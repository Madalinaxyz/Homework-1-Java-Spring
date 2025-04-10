package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.AbstractAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.storage.ClientRepository;

import java.util.List;

public interface Banking {

    Client addClient(Client client);

    Client getClient(String name);

    List<Client> getClients();

    void deleteClient(Client client);

    AbstractAccount createAccount(Client client, Class type);

    void updateAccount(Client c, AbstractAccount account);

    AbstractAccount getAccount(Client client, Class type);

    List<AbstractAccount> getAllAccounts();

    List<AbstractAccount> getAllAccounts(Client client);

    void transferMoney(Client from, Client to, double amount);

    void setRepository(ClientRepository storage);
}
