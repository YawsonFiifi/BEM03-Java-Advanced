package models;

interface Transactable{
    public boolean processTransaction(double amount, String type);
}
