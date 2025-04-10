package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.storage.ClientRepository;

import java.util.List;
import java.util.Map;

public interface BankReportService {

    int getNumberOfBankClients();

    int getAccountsNumber();

    List<Client> getClientsSorted();

    double getBankCreditSum();

    Map<String, List<Client>> getClientsByCity();

    void setRepository(ClientRepository repository);
}
