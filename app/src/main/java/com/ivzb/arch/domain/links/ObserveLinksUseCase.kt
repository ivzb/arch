package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.MediatorUseCase
import com.ivzb.arch.domain.Result
import com.ivzb.arch.model.Link
import javax.inject.Inject

open class ObserveLinksUseCase @Inject constructor(
    private val repository: LinksRepository
) : MediatorUseCase<Unit, List<Link>>() {

    override fun execute(parameters: Unit) {
        result.addSource(repository.observeAll()) {
            result.postValue(Result.Success(it))
        }
    }
}
