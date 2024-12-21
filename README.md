# AlgaWorks - Testes Unitários com Junit (algaworks-unit-tests)

🇧🇷
Repositório para registro de estudos e exercícios de testes unitários com JUnit, baseado no curso "Testes Unitários com JUnit" da AlgaWorks.

🇺🇸
*Repository for registry of study and exercises of unit tests with JUnit, based on AlgaWorks's course "Unit Tests with JUnit".*

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-black?style=for-the-badge&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-black?style=for-the-badge)
![AssertJ](https://img.shields.io/badge/AssertJ-black?style=for-the-badge)
![JACOCO](https://img.shields.io/badge/JACOCO-black?style=for-the-badge)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

## Índice
- [Sobre](#sobre)
- [Tópicos explorados](#tópicos-explorados)
- [Autor](#autor)
- [Agradecimentos](#agradecimentos)
- [Licença](#licença)

## Sobre
Reune exemplos e exercícios que realizei durante o curso "Testes Unitários com JUnit", onde foram exploradas as funcionalidades dos frameworks **JUnit**,
**Mockito**, **AssertJ**, além da biblioteca de análise de cobertura de testes com **JACOCO**, padrões como ***FIRST***, nomenclatura ***BDD***, ***Nest Tests***, ***Object Mother***.

### Objetivos
O objetivo é deixar registrado para consultas futuras e caso seja útil, ajudar também outras pessoas que estejam estudando algo parecido.

### Observações
- Listagem de tópicos está simplificada, de acordo com os desafios que realizei, a listagem dentro do treinamento é muito maior e mais detalhada.

## Tópicos explorados
- JUnit para criar testes unitários
- Boas práticas
- Objetos falsos ou dublês
- Trabalhar com dados fictícios
- Utilizando análise de cobertura de código
- Biblioteca de asserções
- Pirâmide de testes
    - Unit
    - Integration
    - E2E
        - *(Topo)*
        - Complexidade
        - Custo 
        - Tempo de execução
        - *(Base)*
- E2E
    - Mais custosos, tem todo um todo aparato, todas camadas
- Integration
    - Interação entre componentes do sistema entre si
- Unit
    - Componente específico, isolado, independente
- Importância dos testes unitários
    - Testes manuais são demorados
    - Agiliza o teste da aplicação
    - Evitar que bugs ocorram ao alterar o código
    - Documenta funcionalidades do sistema, assim como as regras
    - Dar mais segurança ao refatorar

- O princípio FIRST
    - *Fast*
    - *Independent*
    - *Repeatable*
    - *Self-validating*
    - *Timely*
        >Junto ao desenvolvimento
    - Thorough
        - Minuciosos
        > Um teste, para cada tipo de cenário.
- Testes aninhados (Nested Tests)
- Nomenclatura BDD

- Stub, Mock e Spy
- Stub - Implementação falsa/dublê
- O que é Mock
    - Implementação falsa/fictícia, isolamento de dependências
    - Possui vantagens a mais que o Stub
        - Não é necessário implementar uma interface ou estender uma classe (pode ser criada em tempo de compilação)
        - Facilidade de definir um comportamento fictício dinâmico em uma classe
        - Verificar comportamento da classe com mock
- Mockito + JUnit
- Testes parametrizados
- Análise de cobertura *(Test coverage)*
- JACOCO (Java Code Coverage)
    - Linhas cobertas e não cobertas
    - Regras de negócio = 100%
    - Verificar cobertura de operações CRUD
    >Lacunas = Mostra apenas por onde o teste passou, não leva em conta as asserções.

    >JACOCO opera a nível de *byte-code*, então, mostrará mais métodos na cobertura.

- AssertJ *(Biblioteca de asserções)*
    - Melhora legibilidade
    - Conjunto diverso de asserções
    - Mensagens mais detalhadas
    - Módulos explorados
        - Core
- outros tópicos presentes nos projetos...

- Referências
    - JUnit5
        - https://junit.org/junit5/
    - Mockito
        - https://site.mockito.org/
    - AssertJ
        - https://assertj.github.io/doc/
    - JACOCO
        - https://www.jacoco.org/

## Autor
Leandro Araújo, desenvolvedor Java, com foco em backend.<br>
Busco me aperfeiçoar por meio de cursos e projetos como este, a fim de crescer profissionalmente e humanamente por meio da colaboração.<br><br>
Caso se sinta à vontade, pode entrar em contato:
- https://www.linkedin.com/in/leandroaraujoti/

## Agradecimentos
- https://www.algaworks.com/
- https://blog.algaworks.com/

## Licença
Este projeto é licenciado sob a [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.html).

Para mais detalhes, consulte o arquivo [LICENSE](./LICENSE).

[Voltar ao início](#algaworks---testes-unitários-com-junit-algaworks-unit-tests)