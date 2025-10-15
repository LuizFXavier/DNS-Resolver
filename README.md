
## 1. Descrição

Este projeto implementa um **resolvedor DNS** em Java, capaz de consultar nomes de domínio usando:

- **DNS tradicional (UDP/TCP)** – porta 53  
- **DNS over TLS (DoT)** – porta 853  

O resolvedor suporta:

- Resolução **recursiva ou iterativa**  
- Consulta de registros **A** (IPv4) e **AAAA** (IPv6)  
- Delegações e **CNAMEs encadeados**

---

## 2. Parâmetros suportados

- `--ns` : servidor de nomes a ser consultado (IP ou domínio)  
- `--name` : nome de domínio alvo da consulta (ex.: www.exemplo.com)  
- `--qtype` : tipo de registro solicitado (`A`, `AAAA`)  
- `--mode` : modo de operação do resolver (`recursive`, `iterative`, `validating`, etc.)  
- `--sni` : Server Name Indication, usado em conexões TLS para indicar o hostname  

---
