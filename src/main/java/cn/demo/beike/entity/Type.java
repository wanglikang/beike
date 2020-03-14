package cn.demo.beike.entity;

public class Type {
    private Integer id;

    private String typename;

    private String flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename == null ? null : typename.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}