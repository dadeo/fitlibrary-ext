/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.d4d30.fitlibrary.ext.support;

import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.closure.Closure;
import fitlibrary.collection.CollectionTraverse;
import fitlibrary.diff.Diff_match_patch;
import fitlibrary.dynamicVariable.RecordDynamicVariables;
import fitlibrary.exception.AbandonException;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.exception.FitLibraryShowException;
import fitlibrary.exception.IgnoredException;
import fitlibrary.exception.parse.NoValueProvidedException;
import fitlibrary.parser.Parser;
import fitlibrary.parser.collection.ArrayParser;
import fitlibrary.parser.collection.ListParser;
import fitlibrary.parser.collection.MapParser;
import fitlibrary.parser.collection.SetParser;
import fitlibrary.parser.lookup.GetterParser;
import fitlibrary.parser.lookup.ParseDelegation;
import fitlibrary.parser.lookup.ResultParser;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.traverse.Evaluator;
import fitlibrary.traverse.Traverse;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.typed.TypedObject;
import fitlibrary.utility.TestResults;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

public class AnnotationCalledMethodTarget extends CalledMethodTarget {
    final private Closure closure;
    final private Evaluator evaluator;
    private Parser[] parameterParsers;
    final private Object[] args;
    private String repeatString = null;
    private String exceptionString = null;
    private boolean everySecond = false;
    private int[] argumentIndexes;

    public AnnotationCalledMethodTarget(Closure method, Evaluator evaluator, int[] argumentIndexes) {
        super(method, evaluator);
        this.closure = method;
        this.evaluator = evaluator;
        args = new Object[getParameterTypes().length];
        this.argumentIndexes = argumentIndexes;
        parameterParsers = closure.parameterParsers(evaluator);
    }

    public AnnotationCalledMethodTarget(Evaluator evaluator) {
        super(evaluator);
        this.evaluator = evaluator;
        parameterParsers = new Parser[0];
        args = new Object[0];
        this.closure = null;
    }

    private Class<?> getReturnType() {
        return closure.getReturnType();
    }

    public Object invoke(Cell cell, TestResults testResults) throws Exception {
        collectCell(cell, 0, cell.text(evaluator), testResults, true);
        return invoke(args);
    }

    public TypedObject invokeTyped(Row row, TestResults testResults, boolean catchParseError) throws Exception {
        try {
            collectCells(row, 1, testResults, catchParseError);
        } catch (AbandonException e) {
            throw new IgnoredException(e); // no more to do
        } catch (Exception e) {
            throw new IgnoredException(e); // Unable to call
        }
        try {
            return invokeTyped(args);
        } catch (AbandonException e) {
            throw new IgnoredException(); // no more to do
        }
    }

    public Object invoke(Row row, TestResults testResults, boolean catchParseError) throws Exception {
        try {
            if (everySecond)
                collectCells(row, 2, testResults, catchParseError);
            else
                collectCells(row, 1, testResults, catchParseError);
        } catch (Exception e) {
            throw new IgnoredException(e); // Unable to call
        }
        return invoke(args);
    }

    public Object invokeForSpecial(Row row, TestResults testResults, boolean catchParseError, Cell operatorCell) throws Exception {
        try {
            if (everySecond)
                collectCells(row, 2, testResults, catchParseError);
            else
                collectCells(row, 1, testResults, catchParseError);
        } catch (Exception e) {
            throw new IgnoredException(e); // Unable to call
        }
        try {
            return invoke(args);
        } catch (InvocationTargetException e) {
            Throwable embedded = e.getTargetException();
            if (embedded instanceof FitLibraryShowException)
                operatorCell.error(testResults);
            throw e;
        }
    }

    private void collectCells(Row row, int step, TestResults testResults, boolean catchParseError) throws Exception {
        for (int argNo = 0; argNo < args.length; argNo++) {
            Cell cell = row.cell(argumentIndexes[argNo]);
            collectCell(cell, argNo, cell.text(evaluator), testResults, catchParseError);
        }
    }

    private void collectCell(Cell cell, int argNo, String text, TestResults testResults, boolean catchParseError) throws Exception {
        try {
            args[argNo] = parameterParsers[argNo].parseTyped(cell, testResults).getSubject();
        } catch (Exception e) {
            if (catchParseError) {
                cell.error(testResults, e);
                throw new IgnoredException();
            }
            throw e;
        }
    }

    public void invokeAndCheck(Row row, Cell expectedCell, TestResults testResults, boolean handleSubtype) {
        boolean exceptionExpected = exceptionIsExpected(expectedCell);
        try {
            Object result = invoke(row, testResults, true);
            if (exceptionExpected)
                expectedCell.fail(testResults);
            else
                checkResult(expectedCell, result, true, handleSubtype, testResults);
        } catch (IgnoredException ex) {
            //
        } catch (Exception e) {
            expectedCell.exceptionMayBeExpected(exceptionExpected, e, testResults);
        }
    }

    public void invokeAndCheckForSpecial(Row row, Cell expectedCell, TestResults testResults, Row fullRow, Cell specialCell) {
        boolean exceptionExpected = exceptionIsExpected(expectedCell);
        try {
            Object result = invoke(row, testResults, true);
            if (RecordDynamicVariables.recording() && expectedCell.unresolved(evaluator)) {
                String text = expectedCell.text();
                String key = text.substring(2, text.length() - 1);
                evaluator.setDynamicVariable(key, result.toString());
                RecordDynamicVariables.record(key, result.toString());
                expectedCell.pass(testResults, result.toString());
                return;
            }
            if (exceptionExpected)
                expectedCell.fail(testResults);
            else
                checkResult(expectedCell, result, true, false, testResults);
        } catch (IgnoredException e) {
            //
        } catch (InvocationTargetException e) {
            Throwable embedded = e.getTargetException();
            if (embedded instanceof FitLibraryShowException) {
                specialCell.error(testResults);
                fullRow.error(testResults, e);
            } else
                expectedCell.exceptionMayBeExpected(exceptionExpected, e, testResults);
        } catch (Exception e) {
            expectedCell.exceptionMayBeExpected(exceptionExpected, e, testResults);
        }
    }

    private boolean exceptionIsExpected(Cell expectedCell) {
        return exceptionString != null && exceptionString.equals(expectedCell.text(evaluator));
    }

    public boolean matches(Cell expectedCell, TestResults testResults) {
        try {
            return resultParser.matches(expectedCell, invoke(), testResults);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Defines the Strings that signifies that the value in the row above is
     * to be used again. Eg, it could be set to "" or to '"".
     */
    public void setRepeatAndExceptionString(String repeatString, String exceptionString) {
        this.repeatString = repeatString;
        this.exceptionString = exceptionString;
    }

    public void setEverySecond(boolean everySecond) {
        this.everySecond = everySecond;
    }

    private Object wrapObjectWithTraverse(TypedObject typedResult) {
        Object result = typedResult.getSubject();
        if (result == null)
            return null;
        if (isPrimitiveReturnType())
            return result;
        if (result instanceof String || result instanceof StringBuffer)
            return result;
        if (result instanceof Evaluator) {
            Evaluator resultEvaluator = (Evaluator) result;
            if (resultEvaluator != evaluator && resultEvaluator.getNextOuterContext() == null)
                return withOuter(resultEvaluator);
            return result;
        }
        if (Traverse.getAlienTraverseHandler().isAlienTraverse(result))
            return result;

        Class<?> returnType = result.getClass();
        if (MapParser.applicableType(returnType) || ArrayParser.applicableType(returnType))
            return withOuter(typedResult.traverse(evaluator));
        if (SetParser.applicableType(returnType) || ListParser.applicableType(returnType)) {
            CollectionTraverse traverse = (CollectionTraverse) typedResult.traverse(evaluator);
            traverse.setActualCollection(result);
            return withOuter(traverse);
        }
        if (ParseDelegation.hasParseMethod(returnType))
            return result;
        return withOuter(new DoTraverse(typedResult));
    }

    private Object withOuter(Evaluator inner) {
        inner.setOuterContext(evaluator);
        return inner;
    }

    private boolean isPrimitiveReturnType() {
        Class<?> returnType = getReturnType();
        return returnType.isPrimitive() ||
                returnType == Boolean.class ||
                returnType.isInstance(Number.class) ||
                returnType == Character.class;
    }

    public Object invokeAndWrap(Row row, TestResults testResults) throws Exception {
        return wrapObjectWithTraverse(invokeTyped(row, testResults, true));
    }

    public String getResultString(Object result) throws Exception {
        if (getReturnType() == String.class)
            return (String) result;
        return resultParser.show(result);
    }

    @Override
    public String toString() {
        return "MethodTarget[" + closure + "]";
    }

    public Parser getResultParser() { // TEMP while adding FitLibrary2
        return resultParser;
    }

    public void setResultParser(GetterParser resultAdapter) { // TEMP while adding FitLibrary2
        this.resultParser = resultAdapter;
    }

    public Parser[] getParameterParsers() {
        return parameterParsers;
    }

    public void setParameterParsers(Parser[] parameterAdapters) {
        this.parameterParsers = parameterAdapters;
    }

    public void setTypedSubject(TypedObject typedObject) {
        closure.setTypedSubject(typedObject);
    }

    public boolean returnsVoid() {
        return getReturnType() == void.class;
    }

    public boolean returnsBoolean() {
        return getReturnType() == boolean.class;
    }

    public void color(Row row, boolean right, TestResults testResults) throws Exception {
        for (int i = 0; i < row.size(); ++i) {
            if(Arrays.binarySearch(argumentIndexes, i - 1) < 0) {
                row.cell(i).passOrFail(testResults, right);
            }
        }
    }
}
