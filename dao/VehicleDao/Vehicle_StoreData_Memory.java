package dao.VehicleDao;

import Client.Client;
import Vehicle.Bike;
import Vehicle.Car;
import Vehicle.Vehicle;
import dao.StoreData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vehicle_StoreData_Memory {
  ArrayList<Vehicle> vehicles = new ArrayList<>();
  StoreData mngData = new StoreData();
  File fileVeic = new File("vehicles.txt");
  int nextVehicleId = 0;

  Scanner scanner = new Scanner(System.in);


  public ArrayList<Vehicle> getVeiculos(){
    return vehicles;
  }

  public Vehicle getVehicle(int index){
    return vehicles.get(index);
  }

  //como é normal uma locadora ter o mesmo veículo
  //não será feita uma verificação que compara veículos
  //a diferenciação será dada a partir dos ids que são incrementados automaticamente  
  public void addVeiculo(String tipoToAdd, String nomeToAdd, float diariaToAdd){

    int veichileId = nextId();  //chama função que retorna o último id incrementado
    
    //escreve veículo no txt se o tipo passado for válido
    if(tipoToAdd.equals("moto") || tipoToAdd.equals("Moto")){
      vehicles.add(new Bike(veichileId, nomeToAdd, diariaToAdd));
      mngData.VeicWriter(vehicles);
   } 
    else if(tipoToAdd.equals("carro") || tipoToAdd.equals("Carro")){
      vehicles.add(new Car(veichileId, nomeToAdd, diariaToAdd));
      mngData.VeicWriter(vehicles);
    }
    else
      System.out.println("fail: tipo inválido");

  }

  //Remove o veículo que corresponder ao id contido em idToRemove  
  public void removeVeiculo(int idToRemove){

    int indiceVeic = searchVeic(idToRemove);
    if(indiceVeic != -1){
      vehicles.remove(indiceVeic);
      System.out.println("Removido com sucesso!");
      mngData.updateVeic(vehicles);
    }
    else
      System.out.println("fail: veículo inexistente");       
      
  }

  public void editVehicle(Vehicle updatedVehicle, int indexToUpdate){

    vehicles.set(indexToUpdate, updatedVehicle);
    mngData.updateVeic(vehicles);

  }


  //Interface PORCA
  public void editVeiculo(){

    //Printa as perguntas para o usuário
    //Verifica se o veículo escolhido existe
    //modifica o atributo escolhido
    System.out.println("Qual o Id do veículo que você deseja editar?");
    int idToVerify = scanner.nextInt();
    int indiceVeic = searchVeic(idToVerify);
    if(indiceVeic != -1){   
      
      System.out.println("Escolha o campo que deseja alterar:\n[1]Nome\n[2]Diaria");
      int opcao = scanner.nextInt();
      String espaco = scanner.nextLine(); //solução para scanner anterior não captar o enter

      boolean inputIsValid = false; //varíavel para saber se a opção escolhida é válida    
      if(opcao == 1){

        inputIsValid = true;      
        System.out.print("Digite o novo nome: ");
        String nomeNovo = scanner.nextLine();
        vehicles.get(indiceVeic).setName(nomeNovo);
        mngData.updateVeic(vehicles);

      }
      else if(opcao == 2){
            
        inputIsValid = true;
        System.out.print("Digite a nova diaria: ");
        int novaDiaria = scanner.nextInt();
        vehicles.get(indiceVeic).setDailyRate(novaDiaria);
        mngData.updateVeic(vehicles);
      }
      else
        System.out.println("fail: input inválido");
      
      if(inputIsValid)
        System.out.println("Alterado com sucesso!");
    }    
    else
      System.out.println("fail: veículo não existe");

  }

  public void listVeic(){
    for(int i = 0; i < vehicles.size(); i++)
      System.out.println(vehicles.get(i));
  }



  public int searchVeic(int idToSearch){  

    int indiceVeic = -1; //é igual a -1 quando não encontrado
  
    for(int i = 0; i < vehicles.size(); i++)
      if(idToSearch == vehicles.get(i).getId()){
        indiceVeic = i;
        break;
    }
    
    return indiceVeic;

  }


  public int searchFreeVehicle(){
    int vehicleId = -1;

    for(int i = 0; i < vehicles.size(); i++)
      if(!vehicles.get(i).getIsRented()){
        vehicleId = i;
        break;
      }

    return vehicleId;
  }

                         
  public int nextId (){
    
    int nextId = -1;
    
    try (Scanner veicStream = new Scanner(fileVeic);)
        {
          while(veicStream.hasNext()) { 
            nextId = veicStream.nextInt();
            String tipo = veicStream.nextLine();
            String nome = veicStream.nextLine();
            String diaria = veicStream.nextLine();
            String alugado = veicStream.nextLine();              

            veicStream.nextLine(); // clear buffer before next readLine
          }
        }
        catch(InputMismatchException e) {
          System.out.println("Invalid Input");
        }
        catch(FileNotFoundException e) {
          //System.out.println("\nAviso: Não há dados de veículo");
        }

    return ++nextId;
  }

}