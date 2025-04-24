import com.luxoft.bankapp.exceptions.ActiveAccountNotSet;
import com.luxoft.bankapp.model.AbstractAccount;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankReportService;
import com.luxoft.bankapp.service.BankReportServiceImpl;
import com.luxoft.bankapp.service.Banking;
import com.luxoft.bankapp.model.Client.Gender;
import com.luxoft.bankapp.service.storage.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

@ComponentScan("com.luxoft.bankapp")
@Configuration
@PropertySource("classpath:clients.properties")
public class BankApplication {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment environment;

    private static final String[] CLIENT_NAMES =
            {"Jonny Bravo", "Adam Budzinski", "Anna Smith"};

    public static void main(String[] args) {

//        ClientRepository repository = new MapClientRepository();
//        Banking banking = initialize(repository);
            ApplicationContext context = new AnnotationConfigApplicationContext(BankApplication.class);
            Banking banking = initialize(context);


                workWithExistingClients(context);

                bankingServiceDemo(context);

                bankReportsDemo(context);
    }

    public static void bankReportsDemo(ApplicationContext context) {

        System.out.println("\n=== Using BankReportService ===\n");

//        BankReportService reportService = new BankReportServiceImpl();
//        reportService.setRepository(repository);
//        BankReportService reportService = (BankReportService)context.getBean("bankReport");
            BankReportService reportService =
                context.getBean(BankReportService.class);

        System.out.println("Number of clients: " + reportService.getNumberOfBankClients());

        System.out.println("Number of accounts: " + reportService.getAccountsNumber());

        System.out.println("Bank Credit Sum: " + reportService.getBankCreditSum());
    }

    public static void bankingServiceDemo(ApplicationContext context) {

        System.out.println("\n=== Initialization using Banking implementation ===\n");
        Banking banking = context.getBean(Banking.class);

        Client anna = new Client(CLIENT_NAMES[2], Gender.FEMALE);
        anna = banking.addClient(anna);

        AbstractAccount saving = banking.createAccount(anna, SavingAccount.class);
        saving.deposit(1000);

        banking.updateAccount(anna, saving);

        AbstractAccount checking = banking.createAccount(anna, CheckingAccount.class);
        checking.deposit(3000);

        banking.updateAccount(anna, checking);

        banking.getAllAccounts(anna).stream().forEach(System.out::println);
    }

    public static void workWithExistingClients(ApplicationContext context) {

        System.out.println("\n=======================================");
        System.out.println("\n===== Work with existing clients ======");

        Banking banking=context.getBean(Banking.class);

        Client jonny = banking.getClient(CLIENT_NAMES[0]);

        try {

            jonny.deposit(5_000);

        } catch (ActiveAccountNotSet e) {

            System.out.println(e.getMessage());

            jonny.setDefaultActiveAccountIfNotSet();
            jonny.deposit(5_000);
        }

        System.out.println(jonny);

        Client adam = banking.getClient(CLIENT_NAMES[1]);
        adam.setDefaultActiveAccountIfNotSet();

        adam.withdraw(1500);

        double balance = adam.getBalance();
        System.out.println("\n" + adam.getName() + ", current balance: " + balance);

        banking.transferMoney(jonny, adam, 1000);

        System.out.println("\n=======================================");
        banking.getClients().forEach(System.out::println);
    }

    /*
     * Method that creates a few clients and initializes them with sample values
     */
    public static Banking initialize(ApplicationContext context) {

          Banking banking=context.getBean(Banking.class);

          Client client_1=(Client)context.getBean("client1");
          Client client_2=(Client)context.getBean("client2");

          banking.addClient(client_1);
          banking.addClient(client_2);

        return banking;
    }
    @Bean(name = "checkingAccount2")
    public CheckingAccount getDemoCheckingAccount2()
    {
        double overdraft = Double.valueOf(environment.getProperty("checkingAccount2.overdraft"));
        return new CheckingAccount(overdraft);
    }
    @Bean(name = "checkingAccount1")
    public CheckingAccount getDemoCheckingAccount1()
    {
        double overdraft = Double.valueOf(environment.getProperty("checkingAccount1.overdraft"));
        return new CheckingAccount(overdraft);
    }

    @Bean(name = "savingAccount1")
    public SavingAccount getDemoSavingAccount1()
    {
        double balance = Double.valueOf(environment.getProperty("savingAccount1.balance"));
        return new SavingAccount(balance);
    }
    @Bean(name = "client2")
    public Client getDemoClient2()
    {
        String name = environment.getProperty("client2");
        String city=environment.getProperty("client2.city");

        Client client = new Client(name, Gender.MALE);
        client.setCity(city);

        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount2");

        client.addAccount(checking);

        return client;
    }

    @Bean(name = "client1")
    public Client getDemoClient1()
    {
        String name = environment.getProperty("client1");
        String city=environment.getProperty("client1.city");

        Client client = new Client(name, Gender.MALE);
        client.setCity(city);

        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount1");
        AbstractAccount saving = (SavingAccount)
                applicationContext.getBean("savingAccount1");

        client.addAccount(checking);
        client.addAccount(saving);


        return client;
    }
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("clients.properties"));
        return configurer;
    }

}
