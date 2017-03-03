package web.entity;

public class TestData {
    private String name;
    private int age;

    public TestData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public TestData() {
        this.name = "";
        this.age = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
