package br.com.fourshopp.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import br.com.fourshopp.entities.Cargo;
import br.com.fourshopp.entities.Setor;

public class UtilValidate {
	
	public static String validarCpf(Scanner scanner) {
		Boolean fim = true;
		try {
			while(fim) {
	        	System.out.println("Insira seu cpf: ");
	            String cpf = scanner.nextLine();
	            
	            if(cpf.matches("[0-9]{11}")) {
	            	 char dig10, dig11;
	                 int sm, i, r, num, peso;
	                 
	                     // Calculo do 1o. Digito Verificador
	                         sm = 0;
	                         peso = 10;
	                        for (i=0; i<9; i++) {
	                     // converte o i-esimo caractere do cpf em um numero:
	                     // por exemplo, transforma o caractere '0' no inteiro 0
	                     // (48 eh a posicao de '0' na tabela ASCII)
	                         num = (int)(cpf.charAt(i) - 48);
	                         sm = sm + (num * peso);
	                         peso = peso - 1;
	                         }

	                         r = 11 - (sm % 11);
	                         if ((r == 10) || (r == 11)) {
	                             dig10 = '0';
	                         }else {dig10 = (char)(r + 48);}; // converte no respectivo caractere numerico

	                     // Calculo do 2o. Digito Verificador
	                         sm = 0;
	                         peso = 11;
	                         for(i=0; i<10; i++) {
	                         num = (int)(cpf.charAt(i) - 48);
	                         sm = sm + (num * peso);
	                         peso = peso - 1;
	                         }

	                         r = 11 - (sm % 11);
	                         if ((r == 10) || (r == 11)) {
	                              dig11 = '0';
	                         } else {dig11 = (char)(r + 48);};

	                     // Verifica se os digitos calculados conferem com os digitos informados.
	                         if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
	                        	 fim = false;
	                        	 return cpf;
	                         } else {
	                        	 System.out.println("CPF inválido");
	                         }
	            	
	            } else {
	            	System.out.println("Formato do cpf inválido");
	            }
	        }
		} catch (Exception e) {
		}
		return null;
		
	}
	
	public static String validarPassword(Scanner scanner) {
		Boolean fim = true;
		try {
			while(fim) {
	        	System.out.println("Insira sua password: ");
	            String password = scanner.nextLine();
	            
	            if(password.length() >= 8) {
	            	fim = false;
	            	return password;
	            } else {
	            	System.out.println("A senha deve ter no mínimo 8 caracteres");
	            }
	        }
		} catch (Exception e) {
		}
		return null;
		
	}
	
	public static Integer validarInteiro(Scanner scanner) {
		Boolean fim = true;
			try {
				while(fim) {
					Integer inteiro = scanner.nextInt();
					fim = false;
					return inteiro;
					
				}
			} catch (Exception e) {
				System.out.println("Valor inválido");
			}
		
		
		return null;
	}
	
	public static Double validarDouble(Scanner scanner) {
		Boolean fim = true;
			try {
				while(fim) {
				
					String dobro = scanner.nextLine();
					if(dobro.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")) {
						fim = false;
						return Double.parseDouble(dobro);
					} else {
						System.out.println("Valor inválido");
					}
					
				}
			} catch (Exception e) {
			}
		
		
		return null;
	}
	
	public static Long validarLong(Scanner scanner) {
		Boolean fim = true;
			try {
				while(fim) {
				
					String longo = scanner.nextLine();
					if(longo.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")) {
						fim = false;
						return Long.parseLong(longo);
					} else {
						System.out.println("Valor inválido");
					}
					
				}
			} catch (Exception e) {
			}
		
		
		return null;
	}
	
	public static Date validarData(Scanner scanner, String type) {
		Boolean fim = true;
		String pattern = "(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0" +
		        "[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32" +
		        "|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
		try {
			while(fim) {
				System.out.println("Digite a data de " + type + "(dd/MM/yyyy):" );
				String inDate = scanner.nextLine();
				
				if(inDate.matches(pattern)) {
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			        Date data = formato.parse(inDate);
			        
			        if(data.before(new Date())) {
			        	return data;
			        } else {
			        	System.out.println("Data inválida");
			        }
				} else {
					System.out.println("Formato de data inválido");
				}
			}
		}catch (Exception e) {
		}
		return null;
	}
	
	public static Date validarDataProduto(Scanner scanner, String type) {
		Boolean fim = true;
		String pattern = "(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0" +
		        "[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32" +
		        "|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
		try {
			while(fim) {
				System.out.println("Digite a data de " + type + "(dd/MM/yyyy):" );
				String inDate = scanner.nextLine();
				
				if(inDate.matches(pattern)) {
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			        Date data = formato.parse(inDate);
			        
			        if(data.after(new Date())) {
			        	return data;
			        } else {
			        	System.out.println("Data inválida");
			        }
				} else {
					System.out.println("Formato de data inválido");
				}
			}
		}catch (Exception e) {
		}
		return null;
	}
	
	public static Cargo validarCargo(Scanner scanner) {
		try {
			Boolean fim = true;
			while(fim){
				System.out.println("Escolha o cargo: \n1 - Administrador \n2 - Chefe de seção \n3 - Gerente");
				int resposta = scanner.nextInt();
				switch(resposta) {
				case 1:
					fim = false;
					return Cargo.ADMINISTRADOR;
				case 2:
					fim = false;
					return Cargo.CHEFE_SECAO;
				case 3:
					fim = false;
					return Cargo.GERENTE;
				default:
					System.out.println("Valor inválido");
				}
			}
			
		} catch (Exception e) {
			System.out.println("Escolha inválida");
		}
		return null;
		
	}
	
	public static Setor validarSetor(Scanner scanner) {
		try {
			while(true){
				System.out.println("Escolha o setor: \n1 - Mercearia \n2 - Bazar \n3 - Eletrônicos \n4 - Comercial");
				int resposta = scanner.nextInt();
				switch(resposta) {
				case 1:
					return Setor.MERCEARIA;
				case 2:
					return Setor.BAZAR;
				case 3:
					return Setor.ELETRONICOS;
				case 4:
					return Setor.COMERCIAL;
				default:
					System.out.println("Valor inválido");
				}
			}
			
		} catch (Exception e) {
			System.out.println("Escolha inválida");
		}
		return null;
	}	
}