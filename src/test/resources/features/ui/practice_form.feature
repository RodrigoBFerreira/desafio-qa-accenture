# language: pt
@ui @forms
Funcionalidade: Practice Form
  Como usuário do demoqa
  Quero preencher e submeter o formulário de prática
  Para registrar os dados de um estudante

  Cenário: Preencher e submeter o formulário com valores aleatórios
    Dado que eu acesso o site demoqa
    E escolho a opção "Forms" na página inicial
    E clico no submenu "Practice Form"
    Quando eu preencho todo o formulário com valores aleatórios
    E faço upload do arquivo "upload.txt"
    E submeto o formulário
    Então um popup de confirmação deve ser exibido com os dados enviados
    Quando eu fecho o popup
    Então o popup não deve mais ser exibido
