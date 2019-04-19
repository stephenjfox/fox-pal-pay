1. Having a DSL that lets me get away from side-affects
    * Side affects aren't the problem. Too many side-affects, or careless use of them, is the problem

2. Intermediate objects that represent the states of a transaction, without getting towards a gnarly
  state-machine (like that I've seen at past employers)

3. Consider a sealed class rather than a marker interface for the MoneyRequest types