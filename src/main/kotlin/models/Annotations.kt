package models

annotation class NotTested
annotation class TestedNotComprehensive
annotation class NotCompleted
annotation class Buggy
annotation class RequiresAuth

@RequiresAuth
annotation class MustBeSameUserAsPosterId