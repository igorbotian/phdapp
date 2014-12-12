package ru.spbftu.igorbotian.phdapp.common;

import java.util.Comparator;

/**
 * Фабрика математических примитивов, используемых в кросс-валидации классификатора
 */
public interface MathDataFactory {

    /**
     * Создание прямой по имеющимся декартовым координатам двух точек, лежащих на ней
     *
     * @param a декартовы координаты первой точки, лежащей на прямой
     * @param b декартовы координаты второй точки, лежащей на прямой
     * @return линиия, проходящая через заданные точки
     * @throws java.lang.NullPointerException     если хотя бы одна из точек не задана
     * @throws java.lang.IllegalArgumentException если точки совпадают
     */
    Line newLine(Point a, Point b);

    /**
     * Создание прямой по имеющимся значениям коэффициентов A, B, C
     *
     * @param a коэффициент A
     * @param b коэффициент B
     * @param c коэффициент C
     * @return прямая, имеющая заданные значения коэффициентов
     * @throws java.lang.IllegalArgumentException если коэффициенты A и B одновременно равны нулю
     */
    Line newLine(double a, double b, double c);

    /**
     * Создание точки, заданной в декартовых координатах, в двумерном пространстве
     *
     * @param x         координата по оси абсцисс
     * @param y         координата по оси ординат
     * @param dataClass класс, к которому принадлежит данная точка
     * @return точка, заданная в указанных декартовых координатах
     * @throws java.lang.NullPointerException если класс не задан
     */
    Point newPoint(double x, double y, DataClass dataClass);

    /**
     * Создание точки, заданной в декартовых координатах, в двумерном пространстве
     *
     * @param x координата по оси абсцисс
     * @param y координата по оси ординат
     * @return точка, заданная в указанных декартовых координатах
     */
    Point newPoint(double x, double y);

    /**
     * Создание точки, принадлежащей заданному классу, с указанными полярными координатами
     *
     * @param r         полярный радиус (неотрицательный)
     * @param phi       полярный угол (в радианах)
     * @param dataClass класс, которому принадлежит точка
     * @return точка, заданная в указанных полярных координатах
     * @throws java.lang.NullPointerException если класс не задан
     */
    PolarPoint newPolarPoint(double r, double phi, DataClass dataClass);

    /**
     * Создание точки, принадлежащей заданному классу, с указанными полярными координатами
     *
     * @param r   полярный радиус (неотрицательный)
     * @param phi полярный угол (в радианах)
     * @return точка, заданная в указанных полярных координатах
     */
    PolarPoint newPolarPoint(double r, double phi);

    /**
     * Создание закрытого диапазона значений указанного типа
     *
     * @param lowerBound нижняя граница диапазона
     * @param upperBound верхняя граница диапазона
     * @param comparator необходим для проверки того, что нижняя граница по величине не больше верхней
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException если значение нижней границы больше по величине значение верхней
     */
    <T> Range<T> newRange(T lowerBound, T upperBound, Comparator<T> comparator);

    /**
     * Создание закрытого диапазона целых чисел
     *
     * @param lowerBound нижняя граница диапазона
     * @param upperBound верхняя граница диапазона
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException если значение нижней границы больше по величине значение верхней
     */
    Range<Integer> newRange(int lowerBound, int upperBound);

    /**
     * Создание закрытого диапазона вещественных чисел
     *
     * @param lowerBound нижняя граница диапазона
     * @param upperBound верхняя граница диапазона
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException если значение нижней границы больше по величине значение верхней
     */
    Range<Double> newRange(double lowerBound, double upperBound);
}
