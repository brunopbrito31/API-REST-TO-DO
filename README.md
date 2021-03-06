# API REST Para Cadastro de Usuários e Tarefas para Aplicação TODO - LIST:

TECNOLOGIAS-----------------------

No projeto, optei pela utilização das seguintes tecnologias:

Spring Boot - Framework JAVA WEB,<br>
Spring DATA JPA - Para persistência de dados,<br>
Spring Security - Segurança da Aplicação ,<br>
Java JWT - Para geração dos Tokens JWT,<br>
Banco  de Dados, H2 (Banco em memória para testes) e MySQL,<br>
Biblioteca Lombok - Para Otimizar o tempo de escrita de código e organização,<br>
Spring Validation - Para Poder realizar validações nas camadas de DTO,<br>
Springfox  Swagger 2 - Para realizar a documentação da API.<br>

ARQUITETURA-----------------------

Seguindo as boas práticas, já consolidadas pela comunidade,  utilizei a adoção de 4 camadas principais, sendo elas : <br>
Controllers:  Responsáveis por cuidar do tratamento das requisições, validação de entrada de dados, conversões entre objetos de persistência e objetos DTO,<br>
Services: Classes onde as regras de negócio são aplicadas e o processamento "pesado" acontece,<br>
Repositories: Classes responsáveis pela persistência e recuperação de dados em banco,<br>
Models: Classes que representam as entidades da aplicação, subdividida em:<br>
Auxiliar que são classes auxiliares dentro da aplicação (Paginação e classe  que serve de suporte para armazenar o usuário logado), além delas, temos : entities,  enums e exceptions.<br>
DTO: São classes utilizadas para facilitar a validação de dados e personalizar o retorno de dados e fornecer uma "blindagem a mais"  na API.<br>

Pacotes de configurações:<br>
Configs (Documentação) e  security.<br>

INSTRUÇÕES DE USO:<br>
Após iniciar o projeto, rodar o script "script-primeiro-usuario.sql"  no banco para inserir um usuário no banco, pois apenas 1 usuário logado pode criar outro.<br>

      Dados  do Primeiro Usuário:
	 Login : usuariodeteste@hotmail.com
	 Senha: 1525
**************************************** 

Após criado, utilizar uma aplicação tais como o POSTMAN,Insomnia, entre outros clientes REST e realizar uma chamada utilizando o método POST para a seguinte URL:<br>
http://localhost:8080/login<br>
No corpo da requisição deverá ser passado o json da seguinte forma:<br>

{
   "login":"usuariodeteste@hotmail.com",
   "password":"1525"
}
**************************************** 

![image](https://user-images.githubusercontent.com/73408388/154881968-475e8503-3376-4ac9-b945-e8486f5e4655.png)<br>


A resposta virá no formato: Bearer  ey.... (Token de acesso JWT).<br>
Agora você deverá acessar a seguinte url:  http://localhost:8080/swagger-ui.html e clicar em Authorize, assim que abrir a janela você irá colar o token em api_key e clicar em Authorize.<br>

![image](https://user-images.githubusercontent.com/73408388/154881931-fed4b326-cc83-42f0-9470-ce8b579750a9.png)<br>
 
Parabéns!! Agora você está logado na documentação e já pode realizar testes de todos os os End-Points da API.<br>

Dúvidas? Email: brunopbrito31@hotmail.com, Contato: +55 (71) 99203-8969<br>
