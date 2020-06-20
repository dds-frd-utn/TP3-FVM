package utn.frd.fvm.adapters;

import java.util.Date;

public class Movimiento {
    private int monto;
    private String msg;
    private Date fecha;

    public Movimiento(int monto, String msg, Date fecha) {
        this.monto = monto;
        this.msg = msg;
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
}
