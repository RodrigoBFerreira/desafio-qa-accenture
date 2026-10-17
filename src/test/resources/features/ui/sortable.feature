# language: pt
@ui @sortable
Funcionalidade: Sortable
  Como usuário do demoqa
  Quero reordenar os itens da lista arrastando-os
  Para organizá-los na ordem desejada

  Cenário: Colocar os elementos na ordem crescente utilizando drag and drop
    Dado que eu acesso o site demoqa
    E escolho a opção "Interactions" na página inicial
    E clico no submenu "Sortable"
    Quando eu coloco os elementos na ordem crescente utilizando drag and drop
    Então os elementos devem estar em ordem crescente
