# language: pt
@api
Funcionalidade: Aluguel de livros via API BookStore
  Como usuário da BookStore
  Quero criar minha conta e alugar livros pela API
  Para consultar os livros vinculados ao meu usuário

  Cenário: Criar usuário, autorizá-lo e alugar dois livros
    Dado que eu crio um novo usuário com credenciais válidas
    Quando eu gero um token de acesso para o usuário
    Então o usuário deve estar autorizado
    Quando eu listo os livros disponíveis
    E eu alugo 2 livros de livre escolha
    Então os detalhes do usuário devem conter os livros alugados
