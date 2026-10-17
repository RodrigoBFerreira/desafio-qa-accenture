# language: pt
@ui @windows
Funcionalidade: Browser Windows
  Como usuário do demoqa
  Quero abrir uma nova janela do navegador
  Para visualizar a página de exemplo

  Cenário: Abrir, validar e fechar uma nova janela
    Dado que eu acesso o site demoqa
    E escolho a opção "Alerts, Frame & Windows" na página inicial
    E clico no submenu "Browser Windows"
    Quando eu clico no botão New Window
    Então uma nova janela deve ser aberta
    E a nova janela deve exibir a mensagem "This is a sample page"
    Quando eu fecho a nova janela
    Então devo voltar para a janela original
