package br.edu.ufcg.computacao.p2lp2.hotelcalifornia;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller.ReservasSessionController;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class HotelCaliforniaSistemaS1S2MinTest {

	HotelCaliforniaSistema driver;

	String idClienteA, idClienteB, idGerente, idFuncionario, numQuartoSingle, numQuartoDouble, numQuartoFamily;
	LocalDateTime dataInicio;
	LocalDateTime dataFim;
	String idRefCafeMatinal, idRefAlmoco, idRefJantar;
	String idReservaQuartoSingle, idReservaQuartoDouble, idReservaQuartoFamily;
	String idAuditorio;
	String idReservaAuditorio;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		driver = new HotelCaliforniaSistema();
	}

	@AfterEach
	void tearDown() throws Exception {
		driver = null;
	}

	@Nested
	@DisplayName("US01: Gerenciar Usuarios")
	class US01Validations {

		@Test
		@DisplayName("CA.01.1: Cadastrar Administrador")
		void testCadastrarAdministrador() {
			String resultado = driver.cadastrarUsuario("ADM1", "Novo Administrador", "ADM", 123456L);
			assertTrue(resultado.contains("ADM"));
		}

		@Test
		@DisplayName("CA.01.1: Cadastrar Gerente")
		void testCadastrarGerente() {
			String resultado = driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L);
			assertTrue(resultado.contains("GER"));
		}

		@Test
		@DisplayName("CA.01.1: Usuario Autenticacao nao existe ao cadastrar")
		void testUsuarioAutenticacaoNaoExisteAoCadastrar() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cadastrarUsuario("ADM2", "Novo Gerente", "GER", 123456L);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.01.1: Cadastrar Funcionario")
		void testCadastrarFuncionario() {
			String resultado = driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L);
			assertTrue(resultado.contains("FUN"));
		}

		@Test
		@DisplayName("CA.01.1: Cadastrar Cliente")
		void testCadastrarCliente() {
			String resultado = driver.cadastrarUsuario("ADM1", "Novo Cliente", "CLI", 123456L);
			assertTrue(resultado.contains("CLI"));
		}

		@Test
		@DisplayName("CA.01.2: So deve haver um gerente no hotel")
		void testCadastrarGerenteSohDeveHaverUm() {
			String resultado = driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L);
			assertTrue(resultado.contains("GER"));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cadastrarUsuario("ADM1", "Novo Gerente 2", "GER", 123456L);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("SO DEVE HAVER UM GERENTE NO HOTEL"));
		}

		@Test
		@DisplayName("CA.01.2: Cliente nao pode cadastrar usuario")
		void testClienteNaoPodeCadastrarUsuario() {
			String cliente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Cliente", "CLI", 123456L));
			assertTrue(cliente.contains("CLI"));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cadastrarUsuario(cliente, "Novo Funcionario", "FUN", 123456L);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UM NOVO USUARIO DO TIPO"));
		}

		@Test
		@DisplayName("CA.01.2: Funcionario nao pode cadastrar gerente ou administrador")
		void testFuncionarioNaoPodeCadastrarGerenteOuAdministrador() {
			String funcionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L));
			assertTrue(funcionario.contains("FUN"));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cadastrarUsuario(funcionario, "Novo Gerente", "GER", 123456L);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UM NOVO USUARIO DO TIPO"));
		}

		@Test
		@DisplayName("CA.01.2: Gerente nao pode cadastrar administrador")
		void testGerenteNaoPodeCadastrarGerenteOuAdministrador() {
			String gerente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L));
			assertTrue(gerente.contains("GER"));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cadastrarUsuario(gerente, "Novo Gerente", "ADM", 123456L);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UM NOVO USUARIO DO TIPO"));
		}

		@Test
		@DisplayName("CA.01.3: Identificador Unico")
		void testIdentificadorUnico() {
			String adm2 = driver.cadastrarUsuario("ADM1", "Novo Admin 2", "ADM", 123456L);
			String adm3 = driver.cadastrarUsuario("ADM1", "Novo Admin 3", "ADM", 123456L);
			String adm4 = driver.cadastrarUsuario("ADM1", "Novo Admin 4", "ADM", 123456L);
			assertAll(
					() -> assertTrue(adm2.contains("ADM")),
					() -> assertTrue(adm3.contains("ADM")),
					() -> assertTrue(adm4.contains("ADM")),
					() -> assertNotEquals(adm2, adm3),
					() -> assertNotEquals(adm3, adm4),
					() -> assertNotEquals(adm4, adm2)
			);
		}

		@Test
		@DisplayName("CA.01.4: Atualizar Usuario")
		void testAtualizarUsuario() {
			String funcionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L));
			String resultado = driver.atualizarUsuario("ADM1", funcionario, "CLI");
			assertTrue(resultado.contains("CLI"));
		}

		@Test
		@DisplayName("CA.01.4: Somente Administrador pode atualizar Usuario")
		void testSomenteAdministradorPodeAtualizarUsuario() {
			String funcionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.atualizarUsuario(funcionario, "ADM1", "CLI");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("APENAS O ADMINISTRADOR PODE ATUALIZAR OS USUARIOS"));
		}

		@Test
		@DisplayName("CA.01.4: Atualiza Usuario para cargo de gerente")
		void testAtualizaUsuarioCargoGerente() {
			String gerente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L));
			String funcionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L));
			String resultado = driver.atualizarUsuario("ADM1", funcionario, "GER");
			assertAll(
					() -> assertTrue(resultado.contains("GER")),
					() -> assertNotEquals(gerente, resultado)
			);
		}

		@Test
		@DisplayName("CA.01.4: Administrador nao encontrado durante processo de atualizacao de usuario")
		void testAdminNaoEncontradoDuranteAtualizacao() {
			String funcionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 123456L));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.atualizarUsuario("ADM2", funcionario, "GER");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.01.4: Usuario a ser alterado nao encontrado durante processo de atualizacao de usuario")
		void testUsuarioSerAlteradoNaoEncontradoDuranteAtualizacao() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.atualizarUsuario("ADM1", "FUN1", "GER");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.01.5: Exibir Usuario")
		void testExibir() {
			String gerente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L));
			String resultado = driver.exibirUsuario(gerente);
			assertAll(
					()-> assertTrue(resultado.contains("GER")),
					()-> assertTrue(resultado.contains("Novo Gerente")),
					()-> assertTrue(resultado.contains("123456"))
			);
		}

		@Test
		@DisplayName("CA.01.5: Listar Usuarios")
		void testListar() {
			String gerente = driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L);
			String[] resultado = driver.listarUsuarios();
			assertAll(
					()-> assertEquals(2, resultado.length),
					()-> assertTrue(resultado[0].contains("ADM1")),
					()-> assertTrue(resultado[1].contains(gerente))
			);
		}

	}

	@Nested
	@DisplayName("US02: Disponibilizar Quarto no Hotel")
	class US02Validations {

		@Test
		@DisplayName("CA.02.1/3/4: Tipos de Quarto - Cadastrar quarto Single")
		void testCadastrarQuartoSingle() {
			String resultado = driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0);
			assertAll(
					()-> assertTrue(resultado.contains("Quarto Single")),
					()-> assertTrue(resultado.contains("101")),
					()-> assertTrue(resultado.contains("R$80,00")),
					()-> assertTrue(resultado.contains("R$20,00"))
			);
		}

		@Test
		@DisplayName("CA.02.1: Usuario Autenticacao nao existe ao cadastrar quarto")
		void testUsuarioAutenticacaoNaoExisteAoCadastrarQuarto() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarQuartoSingle("ADM2", 101, 80.0, 20.0);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.02.1: Quarto ja existe")
		void testQuartoJaExiste() {
			driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarQuartoDouble("ADM1", 101, 110.0, 20.0, new String[]{"frigoba"});
			});
			assertTrue(hce.getMessage().toUpperCase().contains("QUARTO JA EXISTE"));
		}

		@Test
		@DisplayName("CA.02.1: Usuario Autenticacao nao e ADMIN ao cadastrar quarto")
		void testUsuarioAutenticacaoNaoAdminAoCadastrarQuarto() {
			String cliente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Cliente", "CLI", 123456L));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarQuartoSingle(cliente, 101, 80.0, 20.0);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E ADMINISTRADOR"));
		}

		@Test
		@DisplayName("CA.02.1/3/4: Tipos de Quarto - Cadastrar quarto Double")
		void testCadastrarQuartoDouble() {
			String resultado = driver.disponibilizarQuartoDouble("ADM1", 201, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"});
			assertAll(
					()-> assertTrue(resultado.contains("Quarto Double")),
					()-> assertTrue(resultado.contains("201")),
					()-> assertTrue(resultado.contains("R$80,00")),
					()-> assertTrue(resultado.contains("R$20,00")),
					()-> assertTrue(resultado.contains("cama extra infantil")),
					()-> assertTrue(resultado.contains("agua quente"))
			);
		}

		@Test
		@DisplayName("CA.02.1/3/4: Tipos de Quarto - Cadastrar quarto Family")
		void testCadastrarQuartoFamily() {
			String resultado = driver.disponibilizarQuartoFamily("ADM1", 301, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"}, 10);
			assertAll(
					()-> assertTrue(resultado.contains("Quarto Family")),
					()-> assertTrue(resultado.contains("301")),
					()-> assertTrue(resultado.contains("R$80,00")),
					()-> assertTrue(resultado.contains("R$20,00")),
					()-> assertTrue(resultado.contains("cama extra infantil")),
					()-> assertTrue(resultado.contains("agua quente")),
					()-> assertTrue(resultado.contains("10 pessoa(s)"))

			);
		}

		@Test
		@DisplayName("CA.02.4: Exibir quarto Single")
		public void exibirQuartoSingle() {
			String quartoIdStr = extrairId(driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0));
			String resultado = driver.exibirQuarto(Integer.valueOf(quartoIdStr));
			assertAll(
					()-> assertTrue(resultado.contains("Single"))
			);
		}

		@Test
		@DisplayName("CA.02.4: Exibir quarto Double")
		public void exibirQuartoDouble() {
			String quartoIdStr = extrairId(driver.disponibilizarQuartoDouble("ADM1", 201, 80.0, 20.0, new String[]{"Frigobar"}));
			assert quartoIdStr != null;
			String resultado = driver.exibirQuarto(Integer.valueOf(quartoIdStr));
			assertAll(
					()-> assertTrue(resultado.contains("Double"))
			);
		}
		@Test
		@DisplayName("CA.02.4: Exibir quarto Family")
		public void exibirQuartoFamily() {
			String quartoIdStr = extrairId(driver.disponibilizarQuartoFamily("ADM1", 301, 80.0, 20.0, new String[]{"Frigobar"}, 6));
			assert quartoIdStr != null;
			String resultado = driver.exibirQuarto(Integer.valueOf(quartoIdStr));
			assertAll(
					()-> assertTrue(resultado.contains("Family"))
			);
		}

		@Test
		@DisplayName("CA.02.2: Valor da Diaria - Quarto Single")
		void testValorDiaraQuartoSingle() {
			String resultado = driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0);
			assertAll(
					()-> assertTrue(resultado.contains("R$100,00"))
			);
		}

		@Test
		@DisplayName("CA.02.2: Valor da Diaria - Quarto Double")
		void testValorDiariaQuartoDouble() {
			String resultado = driver.disponibilizarQuartoDouble("ADM1", 201, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"});
			assertAll(
					()-> assertTrue(resultado.contains("R$120,00"))
			);
		}

		@Test
		@DisplayName("CA.02.2: Valor da Diaria - Quarto Family")
		void testValorDiariaQuartoFamily() {
			String resultado = driver.disponibilizarQuartoFamily("ADM1", 301, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"}, 10);
			assertAll(
					()-> assertTrue(resultado.contains("R$280,00"))
			);
		}

		@Test
		@DisplayName("CA.02.4: Listar Quartos")
		void testListar() {
			String quartoSingle = driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0);
			String quartoDouble = driver.disponibilizarQuartoDouble("ADM1", 201, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"});
			String quartoFamily = driver.disponibilizarQuartoFamily("ADM1", 301, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"}, 10);
			String[] resultado = driver.listarQuartos();
			assertAll(
					()-> assertEquals(3, resultado.length),
					()-> assertTrue(resultado[0].contains(quartoSingle)),
					()-> assertTrue(resultado[1].contains(quartoDouble)),
					()-> assertTrue(resultado[2].contains(quartoFamily))
			);
		}

	}

	@Nested
	@DisplayName("US03: Reservar Quarto")
	class US03Validations {

		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
		}

		@Test
		@DisplayName("CA.03.1/3/6: Reservar Quarto Single e Valor da Total das diarias com refeicao incluida")
		public void reservarQuartoSingle() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			String resultado = driver.reservarQuartoSingle(idGerente, idClienteA, Integer.valueOf(numQuartoSingle),
					dataInicio, dataFim, new String[]{idRefCafeMatinal});
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("Novo Cliente A")),
					() -> assertTrue(resultado.contains("123654")),
					() -> assertTrue(resultado.contains("Quarto Single")),
					() -> assertTrue(resultado.contains("R$70,00")), // Valor Base Quarto
					() -> assertTrue(resultado.contains("R$10,00")), // Valor Pessoa
					() -> assertTrue(resultado.contains("R$80,00")), // Valor Diaria
					() -> assertTrue(resultado.contains("R$20,00")), // Refeicao
					() -> assertTrue(resultado.contains("R$100,00")), // Valor Parcial
					() -> assertTrue(resultado.contains("x2")), // Quantidade diarias
					() -> assertTrue(resultado.contains("R$200,00")) // Valor Total
			);
		}

		@Test
		@DisplayName("CA.03.1/3/6: Reservar Quarto Double e Valor da Total das diarias com refeicao incluida")
		public void reservarQuartoDouble() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			String resultado = driver.reservarQuartoDouble(idFuncionario, idClienteA, Integer.valueOf(numQuartoDouble),
					dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"});
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("Novo Cliente A")),
					() -> assertTrue(resultado.contains("123654")),
					() -> assertTrue(resultado.contains("Quarto Double")),
					() -> assertTrue(resultado.contains("R$100,00")), // Valor Base Quarto
					() -> assertTrue(resultado.contains("R$10,00")), // Valor Pessoa
					() -> assertTrue(resultado.contains("R$120,00")), // Valor Diaria
					() -> assertTrue(resultado.contains("R$20,00")), // Refeicao
					() -> assertTrue(resultado.contains("R$160,00")), // Valor Parcial
					() -> assertTrue(resultado.contains("x2")), // Quantidade diarias
					() -> assertTrue(resultado.contains("R$320,00")), // Valor Total
					() -> assertTrue(resultado.contains("frigoba"))
			);
		}

		@Test
		@DisplayName("CA.03.1/3/6: Reservar Quarto Family e Valor da Total das diarias com refeicao incluida")
		public void reservarQuartoFamily() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			String resultado = driver.reservarQuartoFamily(idGerente, idClienteA, Integer.valueOf(numQuartoFamily),
					dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("Novo Cliente A")),
					() -> assertTrue(resultado.contains("123654")),
					() -> assertTrue(resultado.contains("Quarto Family")),
					() -> assertTrue(resultado.contains("R$150,00")), // Valor Base Quarto
					() -> assertTrue(resultado.contains("R$10,00")), // Valor Pessoa
					() -> assertTrue(resultado.contains("R$200,00")), // Valor Diaria
					() -> assertTrue(resultado.contains("R$20,00")), // Refeicao
					() -> assertTrue(resultado.contains("R$300,00")), // Valor Parcial
					() -> assertTrue(resultado.contains("x2")), // Quantidade diarias
					() -> assertTrue(resultado.contains("R$600,00")), // Valor Total
					() -> assertTrue(resultado.contains("frigoba")),
					() -> assertTrue(resultado.contains("05 pessoa(s)"))
			);
		}

		@Test
		@DisplayName("CA.03.1: Cliente nao pode cadastrar reserva quarto")
		void testClienteNaoPodeCadastrarReservaQuarto() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoFamily(idClienteB, idClienteA, Integer.valueOf(numQuartoFamily),
						dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.03.1: Administrador nao pode cadastrar reserva quarto")
		void testAdmNaoPodeCadastrarReservaQuarto() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoDouble("ADM1", idClienteA, Integer.valueOf(numQuartoDouble),
						dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"});
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.03.2: Disponibilidade da Reserva de Quarto: Ja Existe Reserva")
		public void testReservarNaoEPossivelReservarQuarto() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			String reservaEfetuada = driver.reservarQuartoFamily(idGerente, idClienteA, Integer.valueOf(numQuartoFamily),
					dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			String idResultado = extrairId(reservaEfetuada);
			assertTrue(Integer.parseInt(idResultado)>0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoFamily(idGerente, idClienteB, Integer.valueOf(numQuartoFamily),
						dataInicio.minusDays(1), dataFim.minusDays(1), new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
			hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoFamily(idGerente, idClienteB, Integer.valueOf(numQuartoFamily),
						dataInicio.plusDays(1), dataFim.plusDays(1), new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
		}

		@Test
		@DisplayName("CA.03.4: Periodo de Reserva de Quarto")
		public void testPeriodoReservaDoQuarto() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			String resultado = driver.reservarQuartoFamily(idGerente, idClienteA, Integer.valueOf(numQuartoFamily),
					dataInicio.plusHours(1), dataFim.minusHours(1), new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("06/01/2024 14:00:00 ate 08/01/2024 12:00:00"))
			);
		}

		@Test
		@DisplayName("CA.03.4: Periodo Minimo Antecedencia de Reserva de Quarto")
		public void testPeriodoMinimoReservaQuarto() {
			LocalDateTime today = LocalDateTime.now();
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoFamily(idGerente, idClienteA, Integer.valueOf(numQuartoFamily),
						today.plusHours(12), today.plusDays(2), new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 5);
			});
			assertTrue(hce.getMessage().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));
		}

		@Test
		@DisplayName("CA.03.5: Tentativa de reserva com capacidade excedente")
		public void testReservaCapacidadeExcedente() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 6, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarQuartoFamily(idGerente, idClienteA, Integer.valueOf(numQuartoFamily),
						dataInicio, dataFim, new String[]{idRefCafeMatinal}, new String[]{"frigoba"}, 6);
			});
			assertTrue(hce.getMessage().contains("CAPACIDADE EXCEDIDA"));
		}

	}

	@Nested
	@DisplayName("US04: Disponibilizar Refeicoes do Restaurante")
	class US04Validations {

		@BeforeEach
		public void setUp() {
			usersPreprocessor();
		}

		@Test
		@DisplayName("CA.04.1/2/5: Tipos de Refeicao - Cafe da Manha")
		void testCadastrarRefeicaoCafeDaManha() {
			String resultado = driver.disponibilizarRefeicao(idGerente, "CAFE_DA_MANHA",
					"Cafe Matinal Completo", LocalTime.of(6, 0), LocalTime.of(10, 0),
					20.0, true);
			String idResultado = extrairId(resultado);
			assertAll(
					()-> {
						assert idResultado != null;
						assertTrue(resultado.contains(idResultado));
					},
					()-> assertTrue(resultado.contains("Cafe-da-manha: Cafe Matinal Completo (06h00 as 10h00)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$20,00")),
					()-> assertTrue(resultado.contains("VIGENTE"))
			);
		}

		@Test
		@DisplayName("CA.04.1/2/5: Tipos de Refeicao - Almoco")
		void testCadastrarRefeicaoAlmoco() {
			String resultado = driver.disponibilizarRefeicao(idGerente, "ALMOCO",
					"Almoco de Comida Regional", LocalTime.of(11, 0), LocalTime.of(14, 0),
					80.0, false);
			String idResultado = extrairId(resultado);
			assertAll(
					()-> {
						assert idResultado != null;
						assertTrue(resultado.contains(idResultado));
					},
					()-> assertTrue(resultado.contains("Almoco: Almoco de Comida Regional (11h00 as 14h00)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$80,00")),
					()-> assertTrue(resultado.contains("INDISPONIVEL"))
			);
		}

		@Test
		@DisplayName("CA.04.1/2/5: Tipos de Refeicao - Jantar")
		void testCadastrarRefeicaoJantar() {
			String resultado = driver.disponibilizarRefeicao(idFuncionario, "JANTAR",
					"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
					60.0, true);
			String idResultado = extrairId(resultado);
			assertAll(
					()-> {
						assert idResultado != null;
						assertTrue(resultado.contains(idResultado));
					},
					()-> assertTrue(resultado.contains("Jantar: Comida Italiana (18h00 as 22h30)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$60,00")),
					()-> assertTrue(resultado.contains("VIGENTE"))
			);
		}

		@Test
		@DisplayName("CA.04.1: Usuario Autenticacao nao existe ao cadastrar refeicao")
		void testUsuarioAutenticacaoNaoExisteAoCadastrarRefeicao() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarRefeicao("ADM2", "ALMOCO",
						"Almoco de Comida Regional", LocalTime.of(11, 0), LocalTime.of(14, 0),
						80.0, false);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.04.1: Refeicao ja existe")
		void testRefeicaoJaExiste() {
			driver.disponibilizarRefeicao(idGerente, "JANTAR",
					"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
					60.0, true);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarRefeicao(idFuncionario, "JANTAR",
						"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
						60.0, true);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("REFEICAO JA EXISTE"));
		}

		@Test
		@DisplayName("CA.04.4: Alterar Refeicao - Refeicao nao existe")
		void testAlterarRefeicaoNaoExiste() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.alterarRefeicao(123456789L, LocalTime.of(10,0),
						LocalTime.of(12, 0), 0.0, false);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("REFEICAO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.04.4/3: Alterar Refeicao - Horario de fim deve ser posterior ao horario de inicio")
		void testAlterarRefeicaoHorarioFimPosteriorHorarioInicio() {
			String id = extrairId(driver.disponibilizarRefeicao(idGerente, "CAFE_DA_MANHA",
					"Cafe Matinal Completo", LocalTime.of(6, 0), LocalTime.of(10, 0),
					20.0, true));
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.alterarRefeicao(Long.parseLong(id), LocalTime.of(12,0),
						LocalTime.of(10, 0), 0.0, false);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"));
		}

		@Test
		@DisplayName("CA.04.1: Cliente nao pode cadastrar refeicao")
		void testClienteNaoPodeCadastrarRefeicao() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarRefeicao(idClienteA, "JANTAR",
						"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
						60.0, true);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA REFEICAO"));
		}

		@Test
		@DisplayName("CA.04.1: Administrador nao pode cadastrar refeicao")
		void testAdmNaoPodeCadastrarRefeicao() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarRefeicao("ADM1", "JANTAR",
						"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
						60.0, true);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA REFEICAO"));
		}

		@Test
		@DisplayName("CA.04.3: Horario de fim deve ser posterior ao horario de inicio")
		void testHorarioFimPosteriorInicio() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarRefeicao(idFuncionario, "JANTAR",
						"Comida Italiana", LocalTime.of(22, 30), LocalTime.of(18, 0),
						60.0, true);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"));
		}

		@Test
		@DisplayName("CA.04.4: Alterar Refeicao")
		void testAlterarRefeicao() {
			String idRefeicao = extrairId(driver.disponibilizarRefeicao(idFuncionario, "JANTAR",
					"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
					60.0, true));
			String resultado = driver.alterarRefeicao(Long.parseLong(idRefeicao),
					LocalTime.of(18, 30), LocalTime.of(23, 30),
					70.0, false);
			String idResultado = extrairId(resultado);
			assertAll(
					()-> {
						assert idResultado != null;
						assertTrue(resultado.contains(idResultado));
					},
					()-> assertTrue(resultado.contains("Jantar: Comida Italiana (18h30 as 23h30)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$70,00")),
					()-> assertTrue(resultado.contains("INDISPONIVEL"))
			);
		}

		@Test
		@DisplayName("CA.04.4: Exibir Refeicao")
		void testExibirRefeicao() {
			String idJantar = extrairId(driver.disponibilizarRefeicao(idFuncionario, "JANTAR",
					"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
					60.0, true));
			String resultado = driver.exibirRefeicao(Long.parseLong(idJantar));
			assert resultado != null;
			assertAll(
					()-> assertTrue(resultado.contains(idJantar)),
					()-> assertTrue(resultado.contains("Jantar: Comida Italiana (18h00 as 22h30)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$60,00")),
					()-> assertTrue(resultado.contains("VIGENTE"))
			);
		}

		@Test
		@DisplayName("CA.04.4: Listar Refeicoes")
		public void testListarRefeicoes() {
			driver.disponibilizarRefeicao(idGerente, "JANTAR",
					"Comida Italiana", LocalTime.of(18, 0), LocalTime.of(22, 30),
					60.0, true);
			driver.disponibilizarRefeicao(idFuncionario, "ALMOCO",
					"Comida Brasileira", LocalTime.of(11, 0), LocalTime.of(15, 0),
					100.0, true);
			String[] resultado = driver.listarRefeicoes();
			assertAll(
					() -> assertEquals(2, resultado.length)
			);
		}

	}

	@Nested
	@DisplayName("US05: Reservar Restaurante")
	class US05Validations {
		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
		}

		@Test
		@DisplayName("CA.05.1/3/5/6: Reservar Restaurante e Valor total da reserva")
		public void testReservarRestauranteComCapacidadeAdequada() {
			String resultado = driver.reservarRestaurante(idGerente, idClienteA,
					dataInicio, dataFim, 40, idRefJantar);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado) > 0),
					() -> assertTrue(resultado.contains("Novo Cliente A")),
					() -> assertTrue(resultado.contains("123654")),
					() -> assertTrue(resultado.contains("Qtde. de Convidados: 40 pessoa(s)")),
					() -> assertTrue(resultado.contains("R$100,00")), // Valor por pessoa
					() -> assertTrue(resultado.contains("R$4.000,00")), // Valor Parcial
					() -> assertTrue(resultado.contains("x3")), // Quantidade diarias
					() -> assertTrue(resultado.contains("R$12.000,00")), // Valor Total
					() -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE"))
			);
		}

		@Test
		@DisplayName("CA.05.1: Cliente nao pode cadastrar reserva restaurante")
		void testClienteNaoPodeCadastrarReservaRestaurante() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante(idClienteB, idClienteA,
						dataInicio, dataFim, 50, idRefJantar);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.05.1: Administrador nao pode cadastrar reserva restaurante")
		void testAdminNaoPodeCadastrarReservaRestaurante() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante("ADM1", idClienteA,
						dataInicio, dataFim, 50, idRefJantar);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.05.2: Disponibilidade da Reserva de Restaurante: Ja Existe Reserva")
		public void testNaoEPossivelReservarRestauranteJaExisteReserva() {
			String reservaEfetuada = extrairId(driver.reservarRestaurante(idGerente, idClienteA,
					dataInicio, dataFim, 50, idRefJantar));
			assertTrue(Integer.parseInt(reservaEfetuada)>0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante(idGerente, idClienteB, dataInicio.minusDays(1), dataFim.minusDays(1), 50, idRefJantar);
			});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
			hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante(idGerente, idClienteB, dataInicio.plusDays(1), dataFim.plusDays(1), 50, idRefJantar);
			});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
		}

		@Test
		@DisplayName("CA.05.3: Tentativa de reserva com capacidade excedente")
		public void testReservaCapacidadeExcedente() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante(idGerente, idClienteA, dataInicio, dataFim, 51, idRefJantar);
			});
			assertTrue(hce.getMessage().contains("CAPACIDADE EXCEDIDA"));
		}

		@Test
		@DisplayName("CA.05.4: Periodo Reserva de Restaurante")
		public void testPeriodoReservaRestaurante() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 1, 19, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 3, 20, 0);
			String resultado = driver.reservarRestaurante(idGerente, idClienteA, dataInicio, dataFim, 50, idRefJantar);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("01/01/2024 18:00:00 ate 03/01/2024 22:30:00"))
			);
		}

		@Test
		@DisplayName("CA.05.4: Periodo Minimo Antecedencia de Reserva de Restaurante")
		public void testPeriodoMinimoReservaRestaurante() {
			LocalDateTime today = LocalDateTime.now();
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarRestaurante(idGerente, idClienteA, today.plusHours(12), today.plusHours(14), 50, idRefJantar);
			});
			assertTrue(hce.getMessage().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));
		}

	}

	@Nested
	@DisplayName("US06: Visualizar Reserva")
	class US06Validations {

		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
		}

		@Test
		@DisplayName("CA.06.1: Exibir Reserva: o proprio cliente")
		public void testExibirReservaProprioCliente() {
			String resultado = driver.exibirReserva(idClienteA, Long.parseLong(idReservaQuartoSingle));
			assertAll(
					() -> assertTrue(resultado.contains("Reserva de quarto")),
					() -> assertTrue(resultado.contains("Quarto Single")),
					() -> assertTrue(resultado.contains("custo basico: R$70,00")),
					() -> assertTrue(resultado.contains("por pessoa: R$10,00")), //adicional
					() -> assertTrue(resultado.contains("por pessoa: R$20,00")), //cafe da manha
					() -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$100,00 x2 (diarias) => R$200,00")),
					() -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
			);
		}

		@Test
		@DisplayName("CA.06.1: Exibir Reserva de cliente: gerente")
		public void testExibirReservaDeClienteGerente() {
			String resultado = driver.exibirReserva(idGerente, Long.parseLong(idReservaQuartoDouble));
			assertAll(
					() -> assertTrue(resultado.contains("Reserva de quarto")),
					() -> assertTrue(resultado.contains("Quarto Double")),
					() -> assertTrue(resultado.contains("custo basico: R$100,00")),
					() -> assertTrue(resultado.contains("por pessoa: R$10,00")), //adicional
					() -> assertTrue(resultado.contains("por pessoa: R$80,00")), //almoco
					() -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$280,00 x2 (diarias) => R$560,00")),
					() -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
			);
		}

		@Test
		@DisplayName("CA.06.1: Exibir Reserva de cliente: funcionario")
		public void testExibirReservaDeClienteFuncionario() {
			String resultado = driver.exibirReserva(idFuncionario, Long.parseLong(idReservaQuartoFamily));
			assertAll(
					() -> assertTrue(resultado.contains("Reserva de quarto")),
					() -> assertTrue(resultado.contains("Quarto Family")),
					() -> assertTrue(resultado.contains("custo basico: R$150,00")),
					() -> assertTrue(resultado.contains("por pessoa: R$10,00")), //adicional
					() -> assertTrue(resultado.contains("por pessoa: R$100,00")), //jantar
					() -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$600,00 x2 (diarias) => R$1.200,00")),
					() -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
			);
		}

		@Test
		@DisplayName("CA.06.1: Administrador nao pode exibir reserva de cliente")
		void testAdminNaoPodeExibirReservaDeCliente() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.exibirReserva("ADM1", Long.parseLong(idReservaQuartoSingle));
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
		}

		@Test
		@DisplayName("CA.06.2: Exibir Reserva: Nao Existe")
		void testReservaNaoExiste() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.exibirReserva(idGerente, Long.parseLong(idReservaQuartoSingle+"00"));
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
		}

		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente: o proprio cliente")
		public void testListarReservasAtivasDoClienteProprioCliente() {
			String[] resultado = driver.listarReservasAtivasDoCliente(idClienteA, idClienteA);
			assertAll(
					() -> assertEquals(4, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente: gerente")
		public void testListarReservasAtivasDoClienteGerente() {
			String[] resultado = driver.listarReservasAtivasDoCliente(idGerente, idClienteA);
			assertAll(
					() -> assertEquals(4, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente: Funcionario")
		public void testListarReservasAtivasDoClienteFuncionario() {
			String[] resultado = driver.listarReservasAtivasDoCliente(idFuncionario, idClienteA);
			assertAll(
					() -> assertEquals(4, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente: Administrador (nao autorizado)")
		void testListarReservasAtivasDoClienteAdm() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasDoCliente("ADM1", idClienteA);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
		}
		@Test
		@DisplayName("CA.06.2: Listar Reservas do Cliente: Nao existem reservas")
		void testListarReservasAtivasDoClienteNaoExiste() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasDoCliente(idGerente, idClienteB);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
		}

		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente por Tipo: o proprio cliente")
		public void testListarReservasAtivasDoClientePorTipoProprioCliente() {
			String[] resultado = driver.listarReservasAtivasDoClientePorTipo(idClienteA, idClienteA, "QUARTO");
			assertAll(
					() -> assertEquals(3, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente por Tipo: gerente")
		public void testListarReservasAtivasDoClientePorTipoGerente() {
			String[] resultado = driver.listarReservasAtivasDoClientePorTipo(idGerente, idClienteA, "AUDITORIO");
			assertAll(
					() -> assertEquals(1, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente por Tipo: Funcionario")
		public void testListarReservasAtivasDoClientePorTipoFuncionario() {
			String[] resultado = driver.listarReservasAtivasDoClientePorTipo(idFuncionario, idClienteA, "AUDITORIO");
			assertAll(
					() -> assertEquals(1, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas do Cliente por Tipo: Administrador (nao autorizado)")
		void testListarReservasAtivasDoClientePorTipoAdm() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasDoClientePorTipo("ADM1", idClienteA, "AUDITORIO");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
		}
		@Test
		@DisplayName("CA.06.2: Listar Reservas do Cliente por Tipo: Nao existem reservas")
		void testListarReservasAtivasDoClientePorTipoNaoExiste() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasDoClientePorTipo(idGerente, idClienteB, "QUARTO");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
		}

		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Cliente por Tipo: o proprio cliente")
		public void testListarReservasAtivasPorTipoProprioCliente() {
			driver.reservarQuartoSingle(idGerente, idClienteB, Integer.parseInt(numQuartoSingle),
					dataInicio.minusDays(3), dataFim.minusDays(3), new String[]{idRefCafeMatinal});
			String[] resultado = driver.listarReservasAtivasPorTipo(idClienteA, "QUARTO");
			assertAll(
					() -> assertEquals(4, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Cliente por Tipo: gerente")
		public void testListarReservasAtivasPorTipoGerente() {
			driver.reservarRestaurante(idGerente, idClienteB, dataInicio.minusDays(3), dataFim.minusDays(3),
					10, idRefJantar);
			String[] resultado = driver.listarReservasAtivasPorTipo(idGerente, "RESTAURANTE");
			assertAll(
					() -> assertEquals(1, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Cliente por Tipo: Funcionario")
		public void testListarReservasAtivasPorTipoFuncionario() {
			String[] resultado = driver.listarReservasAtivasPorTipo(idFuncionario, "AUDITORIO");
			assertAll(
					() -> assertEquals(1, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Cliente por Tipo: Administrador (nao autorizado)")
		void testListarReservasAtivasPorTipoAdm() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasPorTipo("ADM1", "AUDITORIO");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
		}
		@Test
		@DisplayName("CA.06.2: Listar Reservas de Todos os Cliente por Tipo: Nao existem reservas")
		void testListarReservasAtivasPorTipoNaoExiste() {
			ReservasSessionController.getInstance().init();
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivasPorTipo(idGerente, "QUARTO");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
		}

		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Clientes: o proprio cliente")
		public void testListarReservasAtivasProprioCliente() {
			driver.reservarQuartoSingle(idGerente, idClienteB, Integer.parseInt(numQuartoSingle),
					dataInicio.minusDays(3), dataFim.minusDays(3), new String[]{idRefCafeMatinal});
			String[] resultado = driver.listarReservasAtivas(idClienteA);
			assertAll(
					() -> assertEquals(5, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Clientes: gerente")
		public void testListarReservasAtivasGerente() {
			driver.reservarRestaurante(idGerente, idClienteB, dataInicio.minusDays(3), dataFim.minusDays(3),
					10, idRefJantar);
			String[] resultado = driver.listarReservasAtivas(idGerente);
			assertAll(
					() -> assertEquals(5, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Clientes: Funcionario")
		public void testListarReservasAtivasFuncionario() {
			driver.reservarAuditorio(idFuncionario, idClienteB, Long.parseLong(idAuditorio),
					dataInicio.minusDays(3), dataFim.minusDays(3), 10);
			String[] resultado = driver.listarReservasAtivas(idFuncionario);
			assertAll(
					() -> assertEquals(5, resultado.length)
			);
		}
		@Test
		@DisplayName("CA.06.1: Listar Reservas de Todos os Clientes: Administrador (nao autorizado)")
		void testListarReservasAtivasAdm() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivas("ADM1");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
		}
		@Test
		@DisplayName("CA.06.2: Listar Reservas de Todos os Clientes: Nao existem reservas")
		void testListarReservasAtivasNaoExiste() {
			ReservasSessionController.getInstance().init();
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.listarReservasAtivas(idGerente);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
		}

	}

	@Nested
	@DisplayName("US07: Disponibilizar Formas de Pagamento")
	class US07Validations {

		@BeforeEach
		public void setUp() {
			usersPreprocessor();
		}

		@Test
		@DisplayName("CA.07.1/3: Tipos de Pagamento - Cartao")
		void testCadastrarFormaDePagamentoCartao() {
			String resultado = driver.disponibilizarFormaDePagamento("ADM1",
					"CARTAO_DE_CREDITO", 0.0);
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(resultado.contains(idResultado)),
					()-> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)"))
			);
		}

		@Test
		@DisplayName("CA.07.1/3: Tipos de Pagamento - PIX")
		void testCadastrarFormaDePagamentoPix() {
			String resultado = driver.disponibilizarFormaDePagamento("ADM1",
					"PIX", 0.05);
			String idResultado = extrairId(resultado);
			assert idResultado != null;assertAll(
					()-> assertTrue(resultado.contains(idResultado)),
					()-> assertTrue(resultado.contains("Forma de pagamento: PIX (5% de desconto em pagamentos)"))
			);
		}

		@Test
		@DisplayName("CA.07.1/3: Tipos de Pagamento - Dinheiro")
		void testCadastrarFormaDePagamentoDinheiro() {
			String resultado = driver.disponibilizarFormaDePagamento("ADM1",
					"DINHEIRO", 0.1);
			String idResultado = extrairId(resultado);
			assert idResultado != null;assertAll(
					()-> assertTrue(resultado.contains(idResultado)),
					()-> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (10% de desconto em pagamentos)"))
			);
		}

		@Test
		@DisplayName("CA.07.1: Usuario Autenticacao nao existe ao cadastrar FormaDePagamento")
		void testUsuarioAutenticacaoNaoExisteAoCadastrarFormaDePagamento() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarFormaDePagamento("ADM2",
						"DINHEIRO", 0.1);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.07.1: Forma De Pagamento ja existe")
		void testFormaDePagamentoJaExiste() {
			driver.disponibilizarFormaDePagamento("ADM1",
					"PIX", 0.05);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarFormaDePagamento("ADM1",
						"PIX", 0.05);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("FORMA DE PAGAMENTO JA EXISTE"));
		}

		@Test
		@DisplayName("CA.07.1: Cliente nao pode cadastrar Forma De Pagamento")
		void testClienteNaoPodeCadastrarFormaDePagamento() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarFormaDePagamento(idClienteA,
						"CARTAO_DE_CREDITO", 0.0);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA FORMA DE PAGAMENTO"));
		}

		@Test
		@DisplayName("CA.07.1: Gerente nao pode cadastrar Forma De Pagamento")
		void testGerenteNaoPodeCadastrarFormaDePagamento() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarFormaDePagamento(idGerente,
						"PIX", 0.05);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA FORMA DE PAGAMENTO"));
		}

		@Test
		@DisplayName("CA.07.1: Funcionario nao pode cadastrar Forma De Pagamento")
		void testFuncionarioNaoPodeCadastrarFormaDePagamento() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarFormaDePagamento(idFuncionario,
						"DINHEIRO", 0.1);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA FORMA DE PAGAMENTO"));
		}

		@Test
		@DisplayName("CA.07.2: Alterar FormaDePagamento")
		void testAlterarFormaDePagamento() {
			String idFormaDePagamento = extrairId(driver.disponibilizarFormaDePagamento("ADM1",
					"DINHEIRO", 0.05));
			assert idFormaDePagamento != null;
			String resultado = driver.alterarFormaDePagamento("ADM1",
					Integer.parseInt(idFormaDePagamento), "DINHEIRO", 0.1);
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertEquals(idFormaDePagamento, idResultado),
					()-> assertTrue(resultado.contains(idResultado)),
					()-> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (10% de desconto em pagamentos)"))
			);
		}

		@Test
		@DisplayName("CA.07.3: Exibir Forma De Pagamento")
		void testExibirFormaDePagamento() {
			String idFormaDePagamento = extrairId(driver.disponibilizarFormaDePagamento("ADM1",
					"DINHEIRO", 0.1));
			String resultado = driver.exibirFormaPagamento(Integer.parseInt(idFormaDePagamento));
			assert resultado != null;
			assertAll(
					()-> assertTrue(resultado.contains(idFormaDePagamento)),
					()-> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (10% de desconto em pagamentos)"))
			);
		}

		@Test
		@DisplayName("CA.07.3: Listar Formas de Pagamento")
		public void testListarFormasDePagamento() {
			driver.disponibilizarFormaDePagamento("ADM1",
					"DINHEIRO", 0.1);
			driver.disponibilizarFormaDePagamento("ADM1",
					"CARTAO_DE_CREDITO", 0.0);
			driver.disponibilizarFormaDePagamento("ADM1",
					"PIX", 0.05);
			String[] resultado = driver.listarFormasPagamentos();
			assertAll(
					() -> assertEquals(3, resultado.length)
			);
		}

	}

	@Nested
	@DisplayName("US08: Pagar Reserva")
	class US08Validations {

		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
			formasDePagamentoPreprocessor();
		}

		@Test
		@DisplayName("CA.08.1/2/5: Realizar pagamento com Cartao de Credito")
		public void testPagarReservaComCartao() {
			String resultado = driver.pagarReservaComCartao(idClienteA, Long.parseLong(idReservaQuartoSingle),
					"Novo Cliente A", "1234432156788765", "05/2033", "010", 12);
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
					()-> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos")),
					()-> assertTrue(resultado.contains("Total Efetivamente Pago: R$200,00 em 12x de R$16,67"))
			);
		}

		@Test
		@DisplayName("CA.08.1/3/5: Realizar pagamento com PIX")
		public void testPagarReservaComPix() {
			String resultado = driver.pagarReservaComPix(idClienteA, Long.parseLong(idReservaQuartoDouble),
					"Novo Cliente A", "12345678900", "001");
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
					()-> assertTrue(resultado.contains("Forma de pagamento: PIX (5% de desconto em pagamentos")),
					()-> assertTrue(resultado.contains("Total Efetivamente Pago: R$532,00 em 1x de R$532,00"))
			);
		}

		@Test
		@DisplayName("CA.08.1/4/5: Realizar pagamento com Dinheiro")
		public void testPagarReservaComDinheiro() {
			String resultado = driver.pagarReservaComDinheiro(idClienteA, Long.parseLong(idReservaQuartoFamily),
					"Novo Cliente A");
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
					()-> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (10% de desconto em pagamentos")),
					()-> assertTrue(resultado.contains("Total Efetivamente Pago: R$1.080,00 em 1x de R$1.080,00"))
			);
		}

		@Test
		@DisplayName("CA.08.1: Somente o proprio cliente pode pagar a sua reserva")
		public void testPagarReservaSomenteProprioCliente() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.pagarReservaComCartao(idClienteB, Long.parseLong(idReservaQuartoSingle),
						"Novo Cliente A", "1234432156788765", "05/2033", "010", 12);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA"));
		}

		@Test
		@DisplayName("CA.08.1: Reserva ja foi paga")
		public void testReservaJaFoiPaga() {
			driver.pagarReservaComDinheiro(idClienteA, Long.parseLong(idReservaQuartoFamily),
					"Novo Cliente A");
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.pagarReservaComDinheiro(idClienteA, Long.parseLong(idReservaQuartoFamily),
						"Novo Cliente A");
			});
			assertTrue(hce.getMessage().toUpperCase().contains("RESERVA JA FOI PAGA"));
		}

	}

	@Nested
	@DisplayName("US09: Cancelar Reserva")
	class US09Validations {

		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
		}

		@Test
		@DisplayName("CA.09.1: Cliente Cancela a sua reserva")
		public void testPagarReservaComDinheiro() {
			String resultado = driver.cancelarReserva(idClienteA, idReservaQuartoDouble);
			assertAll(
					()-> assertTrue(resultado.contains("[CANCELADA]"))
			);
		}

		@Test
		@DisplayName("CA.09.1: Somente o proprio cliente pode cancelar a sua reserva")
		public void testPagarReservaSomenteProprioCliente() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.cancelarReserva(idClienteB, idReservaQuartoSingle);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA CANCELAR A SUA RESERVA"));
		}

	}


	@Nested
	@DisplayName("US10: Disponibilizar Areas Comuns")
	class US10Validations {

		@BeforeEach
		public void setUp() {
			usersPreprocessor();
		}

		@Test
		@DisplayName("CA.10.1/2/5: Tipos de Area Comum - Auditorio")
		void testCadastrarAreaComumAuditorio() {
			String resultado = driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
					LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(Long.parseLong(idResultado) > 0),
					()-> assertTrue(resultado.contains("AUDITORIO: Auditorio Nilo Pecanha (08h00 as 18h00)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: Grátis. Capacidade: 100 pessoa(s). VIGENTE."))
			);
		}

		@Test
		@DisplayName("CA.10.1: Usuario Autenticacao nao existe ao cadastrar AreaComum")
		void testUsuarioAutenticacaoNaoExisteAoCadastrarAreaComum() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum("ADM2","AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
		}

		@Test
		@DisplayName("CA.10.1: Area Comum ja existe")
		void testAreaComumJaExiste() {
			driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
					LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("AREA COMUM JA EXISTE"));
		}

		@Test
		@DisplayName("CA.10.1: Cliente nao pode cadastrar Area Comum")
		void testClienteNaoPodeCadastrarAreaComum() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum(idClienteA,"AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA AREA COMUM"));
		}

		@Test
		@DisplayName("CA.10.1: Gerente nao pode cadastrar Area Comum")
		void testGerenteNaoPodeCadastrarAreaComum() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum(idGerente,"AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA AREA COMUM"));
		}

		@Test
		@DisplayName("CA.10.1: Funcionario nao pode cadastrar Area Comum")
		void testFuncionarioNaoPodeCadastrarAreaComum() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum(idFuncionario,"AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA AREA COMUM"));
		}

		@Test
		@DisplayName("CA.10.3: Horario de fim deve ser posterior ao horario de inicio")
		void testHorarioFimPosteriorInicio() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
						LocalTime.of(8,0), LocalTime.of(7,59), 0.0, true, 100);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"));
		}

		@Test
		@DisplayName("CA.10.4: Alterar Area Comum")
		void testAlterarAreaComum() {
			String idAreaComum = extrairId(driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
					LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100));
			assert idAreaComum != null;
			String resultado = driver.alterarAreaComum("ADM1",
					Long.parseLong(idAreaComum), LocalTime.of(8,30), LocalTime.of(18,30),
					10.0, 150, false);
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertEquals(idAreaComum, idResultado),
					()-> assertTrue(resultado.contains("AUDITORIO: Auditorio Nilo Pecanha (08h30 as 18h30)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$10,00. Capacidade: 150 pessoa(s). INDISPONIVEL."))
			);
		}

		@Test
		@DisplayName("CA.10.5: Exibir Area Comum")
		void testExibirAreaComum() {
			String idAreaComum = extrairId(driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
					LocalTime.of(8,0), LocalTime.of(18,0), 10.0, true, 100));
			assert idAreaComum != null;
			String resultado = driver.exibirAreaComum(Long.parseLong(idAreaComum));
			String idResultado = extrairId(resultado);
			assert idResultado != null;
			assertAll(
					()-> assertTrue(Long.parseLong(idResultado) > 0),
					()-> assertTrue(resultado.contains("AUDITORIO: Auditorio Nilo Pecanha (08h00 as 18h00)")),
					()-> assertTrue(resultado.contains("Valor por pessoa: R$10,00. Capacidade: 100 pessoa(s). VIGENTE."))
			);
		}

		@Test
		@DisplayName("CA.10.5: Listar Areas Comuns")
		public void testListarAreasComuns() {
			driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
					LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100);
			driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Ednaldo do Egipto",
					LocalTime.of(14,0), LocalTime.of(18,0), 0.0, true, 100);
			String[] resultado = driver.listarAreasComuns();
			assertAll(
					() -> assertEquals(2, resultado.length)
			);
		}
	}

	@Nested
	@DisplayName("US11: Reservar Auditorio")
	class US11Validations {

		@BeforeEach
		public void setUp() {
			reservationsPreProcessor();
		}

		@Test
		@DisplayName("CA.11.1/3/5/6: Reservar Auditorio e Valor total da reserva")
		public void testReservarAuditorioComCapacidadeAdequada() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 5, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 7, 12, 0);
			String resultado = driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
					dataInicio, dataFim,  40);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado) > 0),
					() -> assertTrue(resultado.contains("Novo Cliente A")),
					() -> assertTrue(resultado.contains("123654")),
					() -> assertTrue(resultado.contains("Qtde. de Convidados: 40 pessoa(s)")),
					() -> assertTrue(resultado.contains("Grátis")), // Valor por pessoa
					() -> assertTrue(resultado.contains("Grátis")), // Valor Parcial
					() -> assertTrue(resultado.contains("3")), // Quantidade diarias
					() -> assertTrue(resultado.contains("Grátis")), // Valor Total
					() -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO."))
			);
		}

		@Test
		@DisplayName("CA.11.1: Cliente nao pode cadastrar reserva auditorio")
		void testClienteNaoPodeCadastrarReservaAuditorio() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio(idClienteB, idClienteA, Long.parseLong(idAuditorio),
						dataInicio, dataFim, 50);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.11.1: Administrador nao pode cadastrar reserva auditorio")
		void testAdminNaoPodeCadastrarReservaAuditorio() {
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio("ADM1", idClienteA, Long.parseLong(idAuditorio),
						dataInicio, dataFim, 50);
			});
			assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
			assertTrue(hce.getMessage().toUpperCase().contains("CADASTRAR UMA RESERVA"));
		}

		@Test
		@DisplayName("CA.11.2: Disponibilidade da Reserva de Auditorio: Ja Existe Reserva")
		public void testNaoEPossivelReservarAuditorioJaExisteReserva() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 19, 19, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 22, 20, 0);
			String reservaEfetuada = driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
					dataInicio, dataFim, 50);
			String idResultado = extrairId(reservaEfetuada);
			assertTrue(Integer.parseInt(idResultado)>0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio(idGerente, idClienteB, Long.parseLong(idAuditorio),
						dataInicio.minusDays(1), dataFim.minusDays(1), 50);
			});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
			hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio(idGerente, idClienteB, Long.parseLong(idAuditorio),
						dataInicio.plusDays(1), dataFim.plusDays(1), 50);});
			assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
		}

		@Test
		@DisplayName("CA.11.3: Tentativa de reserva com capacidade excedente")
		public void testReservaCapacidadeExcedente() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 5, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 7, 12, 0);
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
						dataInicio, dataFim, 152);
			});
			assertTrue(hce.getMessage().contains("CAPACIDADE EXCEDIDA"));
		}

		@Test
		@DisplayName("CA.11.4: Periodo Reserva de Auditorio")
		public void testPeriodoReservaAuditorio() {
			dataInicio = LocalDateTime.of(2024, Month.JANUARY, 5, 14, 0);
			dataFim = LocalDateTime.of(2024, Month.JANUARY, 7, 12, 0);
			String resultado = driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
					dataInicio, dataFim, 50);
			String idResultado = extrairId(resultado);
			assertAll(
					() -> assertTrue(Integer.parseInt(idResultado)>0),
					() -> assertTrue(resultado.contains("05/01/2024 08:00:00 ate 07/01/2024 18:00:00"))
			);
		}

		@Test
		@DisplayName("CA.11.4: Periodo Minimo Antecedencia de Reserva de Auditorio")
		public void testPeriodoMinimoReservaAuditorio() {
			LocalDateTime today = LocalDateTime.now();
			HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
				driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
						today.plusHours(12), today.plusHours(14), 50);
			});
			assertTrue(hce.getMessage().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));
		}

	}



	private String extrairId(String input) {
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private void reservationsPreProcessor() {
		usersPreprocessor();
		dataInicio = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0);
		dataFim = LocalDateTime.of(2024, Month.JANUARY, 3, 12, 0);
		numQuartoSingle = extrairId(driver.disponibilizarQuartoSingle("ADM1", 101, 70.00, 10.0));
		numQuartoDouble = extrairId(driver.disponibilizarQuartoDouble("ADM1", 201, 100.00, 10.0, new String[]{}));
		numQuartoFamily = extrairId(driver.disponibilizarQuartoFamily("ADM1", 301, 150.00, 10.0, new String[]{}, 5));
		idRefCafeMatinal = extrairId(driver.disponibilizarRefeicao(idGerente, "CAFE_DA_MANHA",
				"Cafe Matinal Completo", LocalTime.of(6, 0), LocalTime.of(10, 0),
				20.0, true));
		idRefAlmoco = extrairId(driver.disponibilizarRefeicao(idGerente, "ALMOCO",
				"Almoco de Comida Regional", LocalTime.of(11, 0), LocalTime.of(14, 0),
				80.0, true));
		idRefJantar = extrairId(driver.disponibilizarRefeicao(idGerente, "JANTAR",
				"Jantar Italiano", LocalTime.of(18, 0), LocalTime.of(22, 30),
				100.0, true));
		idReservaQuartoSingle = extrairId(driver.reservarQuartoSingle(idGerente, idClienteA, Integer.parseInt(numQuartoSingle),
				dataInicio, dataFim, new String[]{idRefCafeMatinal}));
		idReservaQuartoDouble = extrairId(driver.reservarQuartoDouble(idGerente, idClienteA, Integer.parseInt(numQuartoDouble),
				dataInicio, dataFim, new String[]{idRefAlmoco}, new String[]{"cama infantil"}));
		idReservaQuartoFamily = extrairId(driver.reservarQuartoFamily(idGerente, idClienteA, Integer.parseInt(numQuartoFamily),
				dataInicio, dataFim, new String[]{idRefJantar}, new String[]{"frigobar"}, 4));
		idAuditorio = extrairId(driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Nilo Pecanha",
				LocalTime.of(8,0), LocalTime.of(18,0), 0.0, true, 100));
		idReservaAuditorio = extrairId(driver.reservarAuditorio(idGerente, idClienteA, Long.parseLong(idAuditorio),
				dataInicio, dataFim,  40));
	}

	private void formasDePagamentoPreprocessor() {
		driver.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0);
		driver.disponibilizarFormaDePagamento("ADM1", "PIX", 0.05);
		driver.disponibilizarFormaDePagamento("ADM1", "DINHEIRO", 0.10);
	}

	private void usersPreprocessor() {
		idClienteA = extrairId(driver.cadastrarUsuario("ADM1", "Novo Cliente A", "CLI", 123654L));
		idClienteB = extrairId(driver.cadastrarUsuario("ADM1", "Novo Cliente B", "CLI", 321456L));
		idGerente = extrairId(driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123456L));
		idFuncionario = extrairId(driver.cadastrarUsuario("ADM1", "Novo Funcionario do Hotel", "FUN", 654321L));
	}

}