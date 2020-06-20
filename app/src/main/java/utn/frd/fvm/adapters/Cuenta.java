package utn.frd.fvm.adapters;

public class Cuenta {
    private String alias;
    private Integer id;
    private int saldo;

    public Cuenta(Integer id, String alias) {
        this.alias = alias;
        this.id = id;
    }

    public Cuenta(String alias, int saldo) {
        this.alias = alias;
        this.saldo = saldo;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return this.alias;
    }
}
