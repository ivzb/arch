package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.MediatorUseCase
import com.ivzb.arch.domain.Result
import com.ivzb.arch.model.Link
import javax.inject.Inject

open class ObserveLinkUseCase @Inject constructor(
    private val repository: LinksRepository
) : MediatorUseCase<Int, Link>() {

    override fun execute(parameters: Int) {
        result.addSource(repository.observe(parameters)) {
            result.postValue(Result.Success(it))
        }
    }
}
