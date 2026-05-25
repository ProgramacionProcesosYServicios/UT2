package org.example.desarrollo;

public class CuentaBancaria {
    private static final double SALDO_INICIAL = 1000.0;
    private double saldo;

    public CuentaBancaria() {
        this.saldo = SALDO_INICIAL;
    }

    public synchronized double getSaldo() {
        return this.saldo;
    }

    public synchronized void depositar(double cant, String cliente) {
        this.saldo = this.saldo + cant;
        System.out.println(cliente + " ha ingresado " + cant + " euros. Saldo actual: " + this.saldo);
    }

    public synchronized boolean retirar(double cant, String cliente) {
        if (this.saldo >= cant) {
            this.saldo = this.saldo - cant;
            System.out.println(cliente + " ha sacado " + cant + " euros. Saldo actual: " + this.saldo);
            return true;
        } else {
            System.out.println("ERROR: " + cliente + " queria sacar " + cant + " pero no hay suficiente. Saldo: " + this.saldo);
            return false;
        }
    }
}
