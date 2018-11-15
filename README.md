# meeting-room

O Meeting Room é um microserviço para agendamento de salas de reunião

#### Tecnologias Utilizadas
 - Java 8
 - Spring (boot, jpa)
 - H2 (Banco de dados relacional em memória)
 - Junit / Mockito para testes unitários
 - Jacoco (Para verificar a cobertura dos testes unitários)
 
 #### APIs e exemplos para utilização
 
 A aplicação sobe na porta 8080 e pode ser acessada através da URL: http://localhost:8080
 
 #### APIs Para sala de reunião
 
 ##### API para buscar todas salas de reuniões existentes
 
 Request: HTTP METHOD GET http://localhost:8080/room
 
 Response:
 
 ```
 [
     {
         "roomId": 1,
         "roomNumber": 1,
         "roomName": "Sala 01"
     }
 ]
 ```

##### API para incluir uma nova sala de reunião
 
 Request: HTTP METHOD POST http://localhost:8080/room
  
 BODY:
 
 ```
 {
 	"roomNumber": 1,
 	"roomName": "Sala 01"
 }
 ```
 
 Response:
  
  ```
  [
      {
          "roomId": 1,
          "roomNumber": 1,
          "roomName": "Sala 01"
      }
  ]
  ```
  
##### API para editar uma sala de reunião
  
  Request: HTTP METHOD PUT http://localhost:8080/room
    
  BODY:
   
   ```
   {
    "roomId": 1,
   	"roomNumber": 50,
   	"roomName": "Sala Labs 50"
   }
   ```  
  
  Response:
    
  ```
    [
        {
            "roomId": 1,
            "roomNumber": 1,
            "roomName": "Sala 01"
        }
    ]
  ```
 
 ##### API para excluir uma sala de reunião
 
 Request: HTTP METHOD DELETE http://localhost:8080/room/1
 
 OBS: Essa API além de excluir a sala, exclui todos os agendamentos relacionados com a sala
 
 
  #### APIs para Agendamento de sala de reunião
 
  ##### API para buscar todos os agendamentos de todas as salas
  
  Request: HTTP METHOD GET http://localhost:8080/room/scheduling
  
  Response:
  
  ```
  [
      {
          "id": 1,
          "schedulingName": "Teste",
          "initialDate": "2018-11-16T12:30:00",
          "finalDate": "2018-11-16T13:30:00",
          "room": {
              "roomId": 1,
              "roomNumber": 1,
              "roomName": "Sala 01"
          }
      }
  ] 
 ```
 
 ##### API para cadastrar um novo agendamento de sala de reunião
   
   Request: HTTP METHOD POST http://localhost:8080/room/scheduling
   
   BODY:
   ```
   {
   	"schedulingName": "Teste",
   	"scheduledDate":"2018-11-16",
   	"scheduledTime":"12:30:00",
   	"reservedTimeInMinutes": 60,
   	"room": {
           "roomId": 1,
           "roomNumber": 1,
           "roomName": "Sala 01"
       }
   }
   ```
   
   Response:
   
   ```
   [
       {
           "id": 1,
           "schedulingName": "Teste",
           "initialDate": "2018-11-16T12:30:00",
           "finalDate": "2018-11-16T13:30:00",
           "room": {
               "roomId": 1,
               "roomNumber": 1,
               "roomName": "Sala 01"
           }
       }
   ] 
  ```
 
 ##### API para editar um agendamento de sala de reunião
    
  Request: HTTP METHOD PUT http://localhost:8080/room/scheduling
  
  BODY:
      
  ```
    {
      "id": 1,
    	"schedulingName": "Projeto X Labs",
    	"scheduledDate":"2018-11-16",
    	"scheduledTime":"12:30:00",
    	"reservedTimeInMinutes": 90,
    	"room": {
            "roomId": 1,
            "roomNumber": 1,
            "roomName": "Sala 01"
        }
    }
  ```
    
  Response:
    
  ```
    [
        {
            "id": 1,
            "schedulingName": "Projeto X Labs",
            "initialDate": "2018-11-16T12:30:00",
            "finalDate": "2018-11-16T14:00:00",
            "room": {
                "roomId": 1,
                "roomNumber": 1,
                "roomName": "Sala 01"
            }
        }
    ] 
   ```
 
  ##### API para listar e filtrar os agendamentos por data e sala
 
 Request: HTTP METHOD GET http://localhost:8080/room/0/scheduling?initialDate=2018-11-12&finalDate=2018-11-16
 
 
 ##### OBS: O número 0 (zero) no room/0 indica que será buscado os agendamentos em TODAS as salas de reuniões dentro do perído de datas passado.
 
 ##### Caso queira um agendamento de uma sala em específico, passar o número da sala no lugar do zero, e será considerado apenas o campo initialDate para a consulta. Exemplo:
 
 http://localhost:8080/room/7/scheduling?initialDate=2018-11-21&finalDate=2018-11-23
 
 ##### No exemplo acima, será buscado os agendamentos da sala 7 e será considerado apenas a initialDate para a consulta, ou seja, será buscado os agendamentos da sala 7 e no dia 21-11-2018.
 
 Response:
     
   ```
     [
         {
             "id": 1,
             "schedulingName": "Teste",
             "initialDate": "2018-11-16T12:30:00",
             "finalDate": "2018-11-16T13:30:00",
             "room": {
                 "roomId": 1,
                 "roomNumber": 1,
                 "roomName": "Sala 01"
             }
         }
     ] 
  ```
 
 
  ##### API para excluir um agendamento
  
  Request: HTTP METHOD DELETE http://localhost:8080/room/scheduling/1
 
 
 
 #### Regras de Negócio
 
 Observações Gerais:
 
 No arquivo application.properties contêm algumas configurações, incluindo o horário funcional das salas, exemplo: as salas só podem ser agendadas do horário das 10h às 20h. Isso é parametrizável bastando mudar nesse mesmo arquivo.
 
 - Não pode ser reservado uma sala se já houver agendamento para a mesma no mesmo dia e horário;
 - Não pode ser reservado salas aos finais de semana;
 - Não pode ser reservado salas fora do horário útil específicado no arquivo application.properties
 - Não pode ser reservado salas fora do horário minímo e máximo (exemplo: fazer uma reserva de 5 minutos ou fazer uma reserva de 5 dias seguidos)
