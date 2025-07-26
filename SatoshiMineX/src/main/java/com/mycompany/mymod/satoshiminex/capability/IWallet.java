package com.mycompany.mymod.satoshiminex.capability;

/**
 * Кошелёк игрока: хранит балансы BTC и RUB.
 */
public interface IWallet {
    /** Получить баланс в BTC */
    double getBtc();
    /** Получить баланс в RUB */
    double getRub();

    /** Установить баланс в BTC */
    void setBtc(double amount);
    /** Установить баланс в RUB */
    void setRub(double amount);

    /** Добавить указанное количество BTC */
    void addBtc(double amount);
    /** Добавить указанное количество RUB */
    void addRub(double amount);
}
