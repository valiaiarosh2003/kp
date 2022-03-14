package sample;

public class Medicine {
    private String name;
    private String life;
    private String count;
    private String desc;

    public Medicine(String name, String life, String count, String desc) {
        this.name = name;
        this.life = life;
        this.count = count;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
