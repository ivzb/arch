package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

open class LoadLinkUseCase @Inject constructor(
    private val repository: LinksRepository
) : UseCase<Int, Link>() {

    override fun execute(parameters: Int): Link {
        return repository.get(parameters)
    }
}