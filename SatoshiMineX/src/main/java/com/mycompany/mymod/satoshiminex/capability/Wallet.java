package com.mycompany.mymod.satoshiminex.capability;

import net.minecraft.nbt.CompoundTag;

public class Wallet implements IWallet {
    private long btcBalance; // Баланс в сатоши
    private long rubBalance; // Баланс в копейках

    public Wallet() {
        this.btcBalance = 0L;
        this.rubBalance = 0L;
    }

    @Override
    public double getBtc() {
        return btcBalance / 100_000_000.0;
    }

    @Override
    public double getRub() {
        return rubBalance / 100.0;
    }

    @Override
    public void setBtc(double amount) {
        if (amount < 0) throw new IllegalArgumentException("BTC amount cannot be negative");
        this.btcBalance = (long) (amount * 100_000_000);
    }

    @Override
    public void setRub(double amount) {
        if (amount < 0) throw new IllegalArgumentException("RUB amount cannot be negative");
        this.rubBalance = (long) (amount * 100);
    }

    @Override
    public void addBtc(double amount) {
        if (amount < 0) throw new IllegalArgumentException("BTC addition cannot be negative");
        this.btcBalance += (long) (amount * 100_000_000);
    }

    @Override
    public void addRub(double amount) {
        if (amount < 0) throw new IllegalArgumentException("RUB addition cannot be negative");
        this.rubBalance += (long) (amount * 100);
    }

    public long getBtcBalance() {
        return btcBalance;
    }

    public void addBtcBalance(long amount) {
        if (amount < 0) throw new IllegalArgumentException("BTC balance addition cannot be negative");
        this.btcBalance += amount;
    }

    public void removeBtcBalance(long amount) {
        if (amount < 0) throw new IllegalArgumentException("BTC balance removal cannot be negative");
        this.btcBalance = Math.max(0, this.btcBalance - amount);
    }

    public long getRubBalance() {
        return rubBalance;
    }

    public void addRubBalance(long amount) {
        if (amount < 0) throw new IllegalArgumentException("RUB balance addition cannot be negative");
        this.rubBalance += amount;
    }

    public void removeRubBalance(long amount) {
        if (amount < 0) throw new IllegalArgumentException("RUB balance removal cannot be negative");
        this.rubBalance = Math.max(0, this.rubBalance - amount);
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putLong("btcBalance", btcBalance);
        nbt.putLong("rubBalance", rubBalance);
    }

    public void loadNBTData(CompoundTag nbt) {
        btcBalance = nbt.getLong("btcBalance");
        rubBalance = nbt.getLong("rubBalance");
    }
}
