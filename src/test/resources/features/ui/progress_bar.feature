# language: pt
@ui @progressbar
Funcionalidade: Progress Bar
  Como usuário do demoqa
  Quero controlar a barra de progresso
  Para acompanhar o andamento de uma tarefa

  Cenário: Parar a barra antes de 25% e depois completar e resetar
    Dado que eu acesso o site demoqa
    E escolho a opção "Widgets" na página inicial
    E clico no submenu "Progress Bar"
    Quando eu clico em Start e paro a barra antes de 25%
    Então o valor da progress bar deve ser menor ou igual a 25%
    Quando eu clico em Start novamente e aguardo chegar a 100%
    E eu reseto a progress bar
    Então o valor da progress bar deve ser 0%
