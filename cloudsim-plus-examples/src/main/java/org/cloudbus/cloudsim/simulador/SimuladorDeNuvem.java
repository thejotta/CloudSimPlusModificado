/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.simulador;




import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.NetworkTopology;
import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;




/**
 * Classe Simulador de Nuvem
 * @author Anselmo
 */
public class SimuladorDeNuvem {

    private static List<Cloudlet> cloudletList; //Lista de Cloudlett
    private static List<Vm> vmList; //Lista de maquinas virtuais


 
     
     private static void ActiveNetWorkTopology(Datacenter datacenter, DatacenterBroker broker){
        //==========================================================
        //                  Configuração da REDE
        //==========================================================
        //NetworkTopology contém a informação para o comportamento da rede na simulaçã
        //Carrega o arquivo de topologia de rede
        
                    NetworkTopology.buildNetworkTopology("topology.brite");

                    // Mapeia entidades CloudSim para Brite entidades
                    
                    // O Datacenter corresponderá ao BRITE nó 0
                    int briteNode=0;
                    NetworkTopology.mapNode(datacenter.getId(),briteNode);

                    // O Broker corresponderá ao BRITE nó 3
                    briteNode=3;
                    NetworkTopology.mapNode(broker.getId(),briteNode);
                    
        //==========================================================
     }
    
    
    public static void main(String[] args) {
        
        Log.printFormattedLine("Iniciando %s!", SimuladorDeNuvem.class.getSimpleName());
        int Nuser = 1; // N° usuarios da nuvem
        Calendar calendar = Calendar.getInstance(); //Pegando data e hora atual
        boolean trace_flag = false; // trace events
        
        CloudSim.init(Nuser, calendar, trace_flag);//Inicializando Bibliotecas do CloudSim 
        
        //****************************************************************************
        //Criando dataCenter
        DC dc1= new DC("DataCenter_0");
        //CreateHost(mips, ram, storage)
        dc1.CreateHost(1000, 2048, 1000000);
        dc1.CreateHost(1000, 1024, 1000000);
        Datacenter datacenter = dc1.CreateAndGetDatacenter();
        //----------------------------------------------------------------------------
        
        //****************************************************************************
       /*
        * Criando Broker
        * DatacenterBroker modela o agente que age como o mediador entre o usuário e o
        * provedor da nuvem, é através dele que são enviados os Cloudlets para serem executados
        * e também a alocação das máquinas virtuais.
        */
        DatacenterBroker broker =  new DatacenterBrokerSimple("Broker");
        int brokerId = broker.getId(); // Para o primeiro Broker criado o id = 3
        //----------------------------------------------------------------------------
        //****************************************************************************
        /*
         * VM (máquina virtual), que é gerenciada e alocada em uma
         * máquina física (Host)
         */
        VM v = new VM(brokerId);
        v.add(512,250,1);
        v.add(1000,250,1);
        v.add(512,250,1);
        v.add(1000,100,1);
        v.add(512,20,1);
        vmList = v.getList();
        
        //----------------------------------------------------------------------------
        
        //*****************************************************************************
        /*
         * A classe CL (Cloudlet) modela as aplicações que irão ser executadas na nuvem. Possui
         * atributos necessários para a sua execução como tamanho e quantidade de núcleos
         * necessários.
         */
        
            CL cl = new CL(brokerId);
            cl.add(4000, 1);
            cl.add(30000, 1);
            cl.add(40000, 1);
            cl.add(20000, 2);
            cloudletList = cl.getList();
        
        //----------------------------------------------------------------------------
        broker.submitVmList(vmList);
       
        broker.submitCloudletList(cloudletList);
        //ActiveNetWorkTopology(datacenter, broker);

        CloudSim.startSimulation();//Iniciando simulação
        CloudSim.pauseSimulation();
//        List<Cloudlet> newList = broker.getCloudletsFinishedList();
//        CloudletsTableBuilderHelper.print(new TextTableBuilder(), newList);
//        Log.printFormattedLine("%s Finalizado!", SimuladorDeNuvem.class.getSimpleName());
        CloudSim.finishSimulation();
        CloudSim.runStop();
        
    }
    
    
    
    
    
}
