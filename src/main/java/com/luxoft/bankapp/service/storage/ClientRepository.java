package com.luxoft.bankapp.service.storage;

import com.luxoft.bankapp.model.Client;

import java.util.List;

public interface ClientRepository {

    Client add(Client client);

    Client get(long id);

    Client getBy(String name);

    List<Client> getAll();

    Client update(Client o);

    boolean remove(long id);
}
