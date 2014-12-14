package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import ru.spbftu.igorbotian.phdapp.common.BaseDataTest;
import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SvmValidationSampleManagementModule;

/**
 * Класс, базовый для классов, тестирующих математические примитивы, которые используются в кросс-валидации классификатора
 */
public abstract class MathPrimitivesBaseTest<T> extends BaseDataTest<T> {

    private MathDataFactory mathDataFactory;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new DataModule(),
                new SvmValidationSampleManagementModule()
        );

        mathDataFactory = injector.getInstance(MathDataFactory.class);
    }

    protected Line newLine(Point a, Point b) {
        return mathDataFactory.newLine(a, b);
    }

    protected Line newLine(double a, double b, double c) {
        return mathDataFactory.newLine(a, b, c);
    }

    protected Point newPoint(double x, double y, DataClass dataClass) {
        return mathDataFactory.newPoint(x, y, dataClass);
    }

    protected Point newPoint(double x, double y) {
        return mathDataFactory.newPoint(x, y);
    }

    protected PolarPoint newPolarPoint(double r, double phi) {
        return mathDataFactory.newPolarPoint(r, phi);
    }

    protected Range<Integer> newRange(int lowerBound, int upperBound) {
        return mathDataFactory.newRange(lowerBound, upperBound);
    }
}
