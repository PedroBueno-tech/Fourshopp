package br.com.fourshopp.Util;

import br.com.fourshopp.entities.*;
import br.com.fourshopp.service.FuncionarioService;
import br.com.fourshopp.service.OperadorService;
import br.com.fourshopp.service.ProdutoService;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UtilMenu {

    private static double valorTotalCompra;
    private static FuncionarioService funcionarioService = new FuncionarioService();
    // SOU CLIENTE
    public static Cliente menuCadastroCliente(Scanner scanner){
    	

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.nextLine();

        System.out.println("Insira seu celular: ");
        String celular = scanner.nextLine();

        String password = UtilValidate.validarPassword(scanner);
  
        String cpf = UtilValidate.validarCpf(scanner);

        System.out.println("Insira sua rua: ");
        String rua = scanner.nextLine();
        

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        System.out.println("Insira seu numero: ");
        Integer numero = UtilValidate.validarInteiro(scanner);

        System.out.println("Insira sua data de nascimento (dd/MM/yyyy): ");
        Date dataNascimento = UtilValidate.validarData(scanner, "nascimento");

        Endereco endereco = new Endereco(rua, cidade, bairro, numero);
   
        return new Cliente(nome, email, celular, password, cpf, endereco, dataNascimento);

    }


    public static int menuSetor(Scanner scanner) {
        System.out.println("Digite a opção desejada: " +
                "\n1- MERCEARIA \n2- BAZAR \n3- ELETRÔNICOS");
        int opcao = scanner.nextInt();
        return opcao;
    }

    public static void gerarCupomFiscal(Cliente cliente) throws IOException {
        List<Produto> produtos = cliente.getProdutoList();
        Document document = new Document(PageSize.A4);
        File file = new File("CupomFiscal_" + new Random().nextInt() + ".pdf");
        String absolutePath = file.getAbsolutePath();
        PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Image image1 = Image.getInstance("src/main/java/br/com/fourshopp/service/fourshopp.png");
        image1.scaleAbsolute(140f, 140f);
        image1.setAlignment(Element.ALIGN_CENTER);


        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Font total = FontFactory.getFont(FontFactory.HELVETICA);
        total.setSize(12);
        total.setColor(Color.blue);

        Font header = FontFactory.getFont(FontFactory.HELVETICA);
        header.setSize(12);
        header.setFamily("bold");

        document.add(image1);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        ListItem item1 = new ListItem();
        produtos.forEach(produto -> {

            System.out.println(produto.toString());
            Chunk nome = new Chunk("\n" + produto.getNome()+" ("+produto.getQuantidade()+") \nPreço unidade : R$"+df.format(produto.getPreco() / produto.getQuantidade()));
            Phrase frase = new Phrase();
            frase.add(nome);

            Paragraph x = new Paragraph(frase);

            String preco = "............................................................................................................................R$ " + df.format(produto.getPreco());
            Paragraph y = new Paragraph(preco);
            y.setAlignment(Paragraph.ALIGN_RIGHT);
            item1.add(x);
            item1.add(y);

            valorTotalCompra =  valorTotalCompra + produto.getPreco();
        });

        Paragraph paragraph = new Paragraph("\n\nTOTAL: R$" + df.format(valorTotalCompra), total);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);


        document.add(item1);
        document.add(paragraph);


        document.close();
    }
//chefe de setor    
    public static Funcionario cadastrarFuncionario(Scanner scanner) throws ParseException {

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.nextLine();

        System.out.println("Insira seu celular: ");
        String celular = scanner.nextLine();

        String password = UtilValidate.validarPassword(scanner);

        String cpf = UtilValidate.validarCpf(scanner);

        System.out.println("Insira sua rua: ");
        String rua = scanner.nextLine();

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        System.out.println("Insira seu numero: ");
        Integer numero = UtilValidate.validarInteiro(scanner);

        System.out.println("Data de contratação: ");
        Date hireDate = UtilValidate.validarData(scanner, "contratação");

        System.out.println("Insira o salário CLT bruto: ");
        double salario = UtilValidate.validarDouble(scanner);
        
        Cargo cargo = UtilValidate.validarCargo(scanner);
        
        Setor setor = UtilValidate.validarSetor(scanner);
        

        Endereco endereco = new Endereco(rua, cidade, bairro, numero);
        return  new Funcionario(nome, email, celular, password, cpf, endereco, hireDate , cargo, setor, salario, new ArrayList<>(), new ArrayList<>());


    }
    public static Boolean menuDemitirFuncionario(Scanner scanner, List<Funcionario> funcionarios, FuncionarioService service){
    	Boolean fim = true;
		while(fim) {
			try {				
				for (Funcionario funcionario : funcionarios) {
					System.out.println("------------------------------------------"
									  +"ID: " + funcionario.getId() 
									  +"\nNome: " + funcionario.getNome()
									  +"\nCargo: " + funcionario.getCargo()
									  +"\nSetor: " + funcionario.getSetor());
				}
				
				System.out.println("Digite o id do funcionário qual deseja demitir");
				Long idFunc = UtilValidate.validarLong(scanner);
				
				try {
					System.out.println("Você tem certeza que deseja demitir o seguinte funcionário: \n" + service.findById(idFunc).getNome() + "\n1 - Sim | 2 - Não");
					Integer resposta = UtilValidate.validarInteiro(scanner);
					
					if(resposta == 1) {
						service.remove(idFunc);
						System.out.println("Funcionário demitido com sucesso");
						fim = false;
					}	
				} catch (Exception e) {
					System.out.println("Funcionário não encontrado");
				}
				
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return fim;
    }
//cadastrado depois removido
    public static Operador menuCadastrarOperador(Scanner scanner) throws ParseException {

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.nextLine();

        System.out.println("Insira seu celular: ");
        String celular = scanner.nextLine();

        String password = UtilValidate.validarPassword(scanner);

        String cpf = UtilValidate.validarCpf(scanner);

        System.out.println("Insira sua rua: ");
        String rua = scanner.nextLine();

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        System.out.println("Insira seu numero: ");
        int numero = UtilValidate.validarInteiro(scanner);

        Date data =  UtilValidate.validarData(scanner, "contratação");

        System.out.println("Insira o salário CLT bruto: ");
        double salario = UtilValidate.validarDouble(scanner);
        
        Endereco endereco = new Endereco(rua, cidade, bairro, numero);

        return new Operador(nome,email,celular,password,cpf, endereco, data,Cargo.OPERADOR, salario);
    }
    
    public static void alterarEstoqueProduto(Scanner scanner, List<Produto> produtos, ProdutoService service) {
    	// TODO Auto-generated method stub
    	Setor setor = UtilValidate.validarSetor(scanner);
    	
    	for (Produto produto : produtos) {
			if(produto.getSetor() == setor.getCd()) {
				System.out.println("\n-------------------------"
						+ "\nID:" + produto.getId()
						+ "\nNome: " + produto.getNome()
						+ "\nQuantidade" + produto.getQuantidade());
			}
		}
    	System.out.println("Digite o ID do produto qual deseja modificar");
    	Long idProduto = UtilValidate.validarLong(scanner);
    	
    	
    	Produto newProduto = service.findById(idProduto);
    	System.out.println("Deseja atualizar a quantidade do produto? (1 - sim | 2 - não)");
    	int resposta = UtilValidate.validarInteiro(scanner);
    	if(resposta == 1) {
    		System.out.println("Digite a quantidade que deseja adicionar ou retirar do estoque (usar - para retirar)");
        	Integer qttProdutos = UtilValidate.validarInteiro(scanner);
        	
        	newProduto.setQuantidade(qttProdutos);
    	}
    	
    	System.out.println("Deseja atualizar o preço do produto? (1 - sim | 2 - não)");
    	resposta = UtilValidate.validarInteiro(scanner);
    	
    	if(resposta == 1) {
    		System.out.println("Digite o novo preço do produto");
        	Double precoProdutos = UtilValidate.validarDouble(scanner);
        	
        	newProduto.setPreco(precoProdutos);
    	}
    	
    	
    	
    	service.update(newProduto, idProduto);
    	
    	System.out.println("Produto Atualizado com sucesso");
    }
    
    public static Produto cadastrarProduto(Scanner scanner) throws ParseException {
	   try {
		   
		   
		   System.out.println("Digite o nome do produoto: ");
		   scanner.nextLine();
		   String nome = scanner.nextLine();
		   
		   System.out.println("Digite a quantidade de produtos: ");
		   Integer quantidade = UtilValidate.validarInteiro(scanner);
		   
		   System.out.println("Digite o preço do produto: ");
		   Double preco = UtilValidate.validarDouble(scanner);
		   
		   Setor setor = UtilValidate.validarSetor(scanner);
		   
		   scanner.nextLine();
		   Date dataVencimento = UtilValidate.validarDataProduto(scanner, "data de vencimento do produto:");
		   
		   Produto produto = new Produto(nome, quantidade, preco, setor, dataVencimento);
		   return produto;
		   
		   
	   } catch (Exception e) {
		// TODO: handle exception
	}
	   
	   
	return null;
	   
   }


public static void menuCadastrarOperadorChefe(Scanner scanner, Funcionario funcionario, FuncionarioService funcionarioService2, OperadorService operadorService) {
	
	List<Operador> operadores = operadorService.listAll();
	
	System.out.println("\nUsuário logado: ID " + funcionario.getId() + " | " + funcionario.getNome() +
					   "\n\nLista de operadores:");
	
	for (Operador operador : operadores) {
		System.out.println("----------------------------"
						  +"\nID:" + operador.getId()
						  +"\nNome: " + operador.getNome()
						  +"\nChefe: " + (operador.getFuncionario() == null? "" : operador.getFuncionario().getNome()));
	}
	
	System.out.println("\nDigite o id do operador a ser adicionado ao seu time:");
	Long idOperador = UtilValidate.validarLong(scanner);
	
	System.out.println("Digite qual será a carga horária do operador");
	Double carga = UtilValidate.validarDouble(scanner);
	
	Operador operado = operadorService.findById(idOperador);
	operado.setCargaHoraria(carga);
	operado.setFuncionario(funcionario);
	
	operadorService.update(operado, idOperador);
	
	System.out.println("Operador: " + operado.getNome() + "\nCadastrado ao chefe: " + funcionario.getNome());
	
	// TODO Auto-generated method stub
}


public static void excluirProduto(Scanner scanner, ProdutoService produtoService) {
	// TODO Auto-generated method stub
	Setor setor = UtilValidate.validarSetor(scanner);
	
	List<Produto> produtos = produtoService.listAll();
	
	for (Produto produto : produtos) {
		if(produto.getSetor() == setor.getCd()) {
			System.out.println("\n-------------------------"
					+ "\nID:" + produto.getId()
					+ "\nNome: " + produto.getNome()
					+ "\nQuantidade" + produto.getQuantidade());
		}
	}
	System.out.println("Digite o ID do produto qual deseja modificar");
	Long idProduto = UtilValidate.validarLong(scanner);
	
	Produto produto = produtoService.findById(idProduto);
	
	System.out.println("você tem certeza que deseja excluir o produto: " + produto.getNome() + "(1 - Sim | 2 - Não");
	Integer resposta =  UtilValidate.validarInteiro(scanner);
	
	if(resposta == 1) {
		produtoService.remove(idProduto);
		System.out.println("");
	}
	
}



}
