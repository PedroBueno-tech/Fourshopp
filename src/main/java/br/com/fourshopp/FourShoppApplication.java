package br.com.fourshopp;

import br.com.fourshopp.Util.UtilMenu;
import br.com.fourshopp.Util.UtilValidate;
import br.com.fourshopp.entities.*;
import br.com.fourshopp.service.ClienteService;
import br.com.fourshopp.service.FuncionarioService;
import br.com.fourshopp.service.OperadorService;
import br.com.fourshopp.service.ProdutoService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.fourshopp.Util.UtilMenu.*;

@SpringBootApplication
public class FourShoppApplication implements CommandLineRunner {

    Scanner scanner = new Scanner(System.in);

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FuncionarioService funcionarioService;


    private Cliente cliente;

    public static void main(String[] args) {
        SpringApplication.run(FourShoppApplication.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
    	while(true) {
    		System.out.println("====== BEM-VINDO AO FOURSHOPP ======");
            System.out.println("1- Sou cliente \n2- Área do ADM \n3- Seja um Cliente \n4- Login funcionário \n5- Encerrar ");
            int opcao = scanner.nextInt();
            menuInicial(opcao);
    	}
    	
    }

    public void menuInicial(int opcao) throws CloneNotSupportedException, IOException, ParseException {
    	
        if(opcao == 1){
        	scanner.nextLine();
            System.out.println("Insira seu cpf: ");
            String cpf = scanner.nextLine();
            System.out.println("Insira sua senha: ");
            String password = scanner.nextLine();
            this.cliente = clienteService.loadByEmailAndPassword(cpf, password).orElseThrow(() -> new ObjectNotFoundException(1L,"Cliente"));
            if(cliente == null){
                System.err.println("Usuario não encontrado !");
            }

            int contador = 1;
            while (contador == 1) {
                System.out.println("Escolha o setor: ");
                int setor = menuSetor(scanner);


                List<Produto> collect = produtoService.listaProdutosPorSetor(setor).stream().filter(x -> x.getSetor() == setor).collect(Collectors.toList());
                collect.forEach(produto -> {
                    System.out.println(produto.getId()+"- "+produto.getNome()+" Preço: "+produto.getPreco()+" Estoque - "+produto.getQuantidade());
                });

                System.out.println("Informe o número do produto desejado: ");
                Long produto = scanner.nextLong();

                System.out.println("Escolha a quantidade");
                int quantidade = scanner.nextInt();

                // Atualiza estoque
                Produto foundById = produtoService.findById(produto);
                produtoService.diminuirEstoque(quantidade, foundById);

                Produto clone = foundById.clone();
                System.out.println(clone.toString());
                clone.getCalculaValor(quantidade, clone);
                cliente.getProdutoList().add(clone);
                System.out.println("Deseja outro produto S/N ?");
                String escolha  = scanner.next();

                if(!escolha.equalsIgnoreCase("S")) {
                    contador = 2;
                    gerarCupomFiscal(cliente);
                    System.out.println("Gerando nota fiscal...");
                    System.err.println("Fechando a aplicação...");
                }
            }
        }

        if(opcao == 2){
        	Boolean achou = false;
        	Optional<Funcionario> administrador = null;
            System.out.println("INTRANET FOURSHOPP....");

            System.out.println("Insira as credenciais do usuário administrador: ");
            
            try {
            	System.out.println("Insira o seu CPF: ");
                scanner.nextLine();
                String cpf = scanner.nextLine();
                
                System.out.println("Digite o seu password:");
                String password = scanner.nextLine();
                
                administrador = this.funcionarioService.loadByCpfAndPassword(cpf, password);
                
                if(!administrador.isEmpty()) {
                	achou = true;
                } else {
                	System.out.println("Cpf ou senha inválidos");
                }
            } catch (Exception e) {
            	System.out.println("Cpf ou senha inválidos");
			}
            if(achou) {
            	 if((administrador.get().getCargo() != Cargo.ADMINISTRADOR)) {
                 	System.out.println("Você não é um administrador");
                 } else {
                 	Boolean fim = true;
                 	while(fim) {
                 		try {
                 			System.out.println("\n1- Cadastrar funcionários \n2 - Demitir Funcionário \n3 - Cadastrar Operador \n4 - Demitir Operador \n5 - Sair");
                            int escolhaAdm = UtilValidate.validarInteiro(scanner);
                            switch(escolhaAdm) {
                            case 1:
                            	Funcionario funciona = cadastrarFuncionario(scanner);
                                this.funcionarioService.create(funciona);
                                System.out.println("Funcionário cadastrado com sucesso");
                                break;
                            case 2:
        						List<Funcionario> funcionarios = funcionarioService.listAll();
        						System.out.println("b");
                            	UtilMenu.menuDemitirFuncionario(scanner, funcionarios, funcionarioService);
                            	break;
                            case 3:
                            	Operador operador =  menuCadastrarOperador(scanner);
                                this.operadorService.create(operador);
                                System.out.println("Operador cadastrado com sucesso");
                                break;
                            case 4:
                            	break;
                            case 5:
                            	fim = false;
                            	break;
                            default: 
                            	System.out.println("escolha inválida");
                            	break;
                            }
                 			
                 		}catch (Exception e) {
							// TODO: handle exception
						}
                 		
                 		
                 	}
                 }
            }
            
        }

        if(opcao == 3) {
            Cliente cliente = menuCadastroCliente(scanner);
            this.clienteService.create(cliente);
            System.out.println("Bem-vindo, " + cliente.getNome());
        }

        if(opcao == 4){
        	Optional<Funcionario> funcionario = null;
        	System.out.println("Área do funcionário");

            System.out.println("1- Chefe  \n2- Operador ");
            scanner.nextLine();
            Integer escolha = UtilValidate.validarInteiro(scanner);

            System.out.println("Insira seu cpf: ");
            String cpf = scanner.next();

            System.out.println("Insira sua password: ");
            String password = scanner.next();
            try {
            	funcionario = this.funcionarioService.loadByCpfAndPassword(cpf, password);
            } catch (Exception e) {
            	
            }
            
            
            if(funcionario.isEmpty()) {
            	System.out.println("Usuário ou senha inválidos");
            	
            } else if(escolha == 1) {
            	 if (funcionario.get().getCargo() == Cargo.CHEFE_SECAO){
                     System.out.println("Escolha uma opção: \n1 - Cadastrar Produtos \n2 - Alterar estoque do produto \n3 - Excluir produto \n4 - Cadastrar operadores \n5 - Sair");
                     escolha = UtilValidate.validarInteiro(scanner);
                     
                     switch(escolha) {
                     case 1:
                     	System.out.println("Cadastrar produto");
                         Produto produto = UtilMenu.cadastrarProduto(scanner);
                         produtoService.create(produto);
                         System.out.println("Produto " + produto.getNome() + " cadastrado");
                     	break;
                     case 2:
                    	 System.out.println("Alterar estoque do produto");
                    	 List<Produto> produtos = produtoService.listAll();
                    	 UtilMenu.alterarEstoqueProduto(scanner, produtos, produtoService);
                    	 break;
                     case 3:
                    	 System.out.println("Excluir produto");
                    	 UtilMenu.excluirProduto(scanner, produtoService);
                     	 break;
                     case 4:
                     	System.out.println("Cadastrar operadores");
                        UtilMenu.menuCadastrarOperadorChefe(scanner, funcionario.get(), funcionarioService, operadorService);
                        break;
                     case 5:
                     	System.out.println("Tenha um bom trabalho");
                     	break;
                     default:
                     	System.out.println("escolha inválida");
                     	break;
                        
                     }
            	 }
            }else if (escolha == 2) {
            	Optional<Operador> operador = this.operadorService.loadByCpfAndPassword(cpf, password);
                //System.out.println(operador.get());
            }
        if(opcao == 5) {
        	System.out.println("Obrigado por utilizar o programa");
        	System.exit(0);
        }
    }

    }
}
