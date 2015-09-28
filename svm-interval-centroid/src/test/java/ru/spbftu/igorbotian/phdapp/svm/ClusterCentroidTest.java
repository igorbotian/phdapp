package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульный тест для класса <code>ClusterCentroid</code>
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ClusterCentroidTest {

    /**
     * Фабрика объектов предметной области
     */
    private DataFactory dataFactory;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new DataModule());
        dataFactory = injector.getInstance(DataFactory.class);
    }

    @Test
    public void testDistanceBetweenClusterCentroids() {
        Set<? extends UnclassifiedObject> first = newCluster(
                newPoint(0, 0), newPoint(2, 0), newPoint(0, 2), newPoint(2, 2)
        );

        Set<? extends UnclassifiedObject> second = newCluster(
                newPoint(4, 0), newPoint(6, 0), newPoint(4, 2), newPoint(6, 2)
        );

        Assert.assertEquals(4.0, ClusterCentroid.computeDistanceBetween(first, second), 0.1);
    }

    private Set<? extends UnclassifiedObject> newCluster(UnclassifiedObject... items) {
        Objects.requireNonNull(items);
        return Stream.of(items).collect(Collectors.toSet());
    }

    private UnclassifiedObject newPoint(int x, int y) {
        return dataFactory.newUnclassifiedObject(
                String.format("(%d, %d)", x, y),
                Stream.of(
                        dataFactory.newParameter("x", (double) x, BasicDataTypes.REAL),
                        dataFactory.newParameter("y", (double) y, BasicDataTypes.REAL)
                ).collect(Collectors.toSet())
        );
    }
}
