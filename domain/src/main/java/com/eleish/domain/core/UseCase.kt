package com.eleish.domain.core

abstract class UseCase<in Param, out Type : Any> {
    abstract suspend operator fun invoke(params: Param): Type
}