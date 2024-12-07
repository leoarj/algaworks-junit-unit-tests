Objetivos

- JUnit para criar testes unitários
- Boas práticas
- Objetos falsos ou dublê
- Trabalhar com dados fictícios
- Utilizando análise de cobertura de código
- Biblioteca de asserções

Pirâmide de testes
- Unit
- Integration
- E2E

    (Topo)
    Complexidade
    Custo 
    Tempo de execução
    (Base)

E2E
    Mais custosos, todo aparato, todas camadas
Integration
    Interação entre componentes do sistema entre si
Unit
    Componente específico, isolado, independente

Importância dos testes unitários
    Testes manuais são demoradis
    Agiliza o teste da aplicação
    Evitar que bugs ocorram ao alterar o código
    Documenta funcionalidades do sistema, assim como as regras
    Dar mais segurança ao refatorar

O princípio FIRST
Fast
Independent
Repeatable
Self-validating
Timely
    junto ao desenvolvimento
Thorough
    Minuciosos

    Um teste, para cada tipo de cenário

mvn validate
mvn dependency:resolve
mvn compile
mvn verify
mvn package
mvn install
mvn clean

https://maven.apache.org/run.html


JUnit
    Framework para testes
    https://junit.org/junit5/docs/current/user-guide/

Executar exeções em modo debug:
Break points
Run Debug
Step Into etc...
Útil para maior detalhamento