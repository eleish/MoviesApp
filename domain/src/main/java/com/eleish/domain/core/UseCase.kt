package com.eleish.domain.core

import com.eleish.entities.MoviesPage
import com.eleish.entities.Result

abstract class UseCase<in Param, out Type : Any> {
    abstract suspend operator fun invoke(params: Param): Result<MoviesPage>
}