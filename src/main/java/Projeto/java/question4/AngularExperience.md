# Experiência com Angular

## Principais casos de uso
1. ngModel que faz o two-way data binding que atualiza o valor do front de acordo com a mudança de estado
2. Controlar o fluxo de vida da aplicaçao que é ngOnInit que é execultado apos a inicialização, ngOnChanges que é chamado quando ha mudancas, ngAfterViewinit 
que é disparado apos a renderização.
3. E comparaçao com react, a organizacao/estrutura do angular é melhor

```typescript
<div class="vendas-dialog__main two-columns">
<div>
    <span class="vendas-dialog__label">Valor</span>
    <p class="vendas-dialog__value" *ngIf="!isMerchant">R$ {{data.amount | number: '1.2-2'}}</p>
<p class="vendas-dialog__value" *ngIf="isMerchant && data.details[0]">R$ {{data.details[0].divisionValue| number: '1.2-2'}}</p>
</div>
<div>
<span class="vendas-dialog__label">Estabelecimento</span>
<ng-container *ngIf="isMerchant && data.details[0]">
<p class="vendas-dialog__text" *ngIf="isMerchant && data.details[0]">{{data.details[0].merchantName}}</p>
<p class="vendas-dialog__description" *ngIf="isMerchant && data.details[0]">{{data.details[0].merchantDocument | cpfcnpj}}</p>
</ng-container>

<ng-container *ngIf="!isMerchant">
<p class="vendas-dialog__text">{{formatName(data.merchantName)}}</p>
<p class="vendas-dialog__description">{{data.merchantDocument | cpfcnpj}}</p>
</ng-container>
</div>
</div>
```

## Exemplo de Aplicação do Mundo Real

Eu usei Angular para trabalhar em um sistema de pagamentos, onde clientes tinham total controle das duas empresas... As informaçoes financeiras, os tipos de pagamento, provedores de pagamento
tela de vendas e etc


Uma das telas de dashboard em Angular ->

![Captura de Tela 2025-05-20 às 14 37 19](https://github.com/user-attachments/assets/c1af481c-ef0f-4fbb-9e77-35f8839eaf16)








