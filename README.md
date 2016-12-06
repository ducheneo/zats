# zats
ZK Application Test Suite

Here is my modified version of the Zats library, with adaptation to make it work in our Spring Boot environment.

## What did I create ?

Basically 2 spring components `SpringBootZatsEnvironment` en `SpringBootEmulator` that must be used in your test code. 
This Zats version now have direct dependency on Spring libraries.

## How does it work ?

First create a specific `ApplicationTest.java` for the test environment, with a profile specific name (here _specific-profile_).

```java
@Profile("specific-profile")
@SpringBootApplication
@ComponentScan({"be.liege.cti.interets","org.zkoss.zats.mimic.impl.emulator","org.zkoss.zats.mimic"})
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration.class,
        ZKSecurityConfig.class, AppConfig.class})

public class ApplicationTest {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ApplicationTest.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
...
```

Then you can use your test with this configuration and instantiate the environment à la Spring..

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ApplicationTest.class,webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration.class })
@ActiveProfiles({"specific-profile","dev"})
public class IndexComposerTest{

	@Value("${local.server.port}")
	private String serverPort;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private SpringBootZatsEnvironment env;

	@Before
	public void init() {
		env.setContextPath("/");
		env.init(servletContext);

	}

	@SuppressWarnings("unused")
	@Test
	public void testAvantType60() {
		env.getEmulator().setPort(Integer.parseInt(serverPort));
		Client client = env.newClient();
		DesktopAgent desktop = client.connect("/marche1.zul");
    ...
   }

```

Good luck !
