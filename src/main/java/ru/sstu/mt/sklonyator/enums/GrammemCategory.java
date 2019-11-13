package ru.sstu.mt.sklonyator.enums;

public enum GrammemCategory {
    GENDER("род"),
    PERSON("лицо"),
    NUMBER("число"),
    CASE("падеж"),
    TENSE("время"),
    TYPE("вид"),
    DEGREE("степень");

    public final String name;

    GrammemCategory(String name) {
        this.name = name;
    }
}
