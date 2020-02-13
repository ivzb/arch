package com.ivzb.arch.domain.links

import androidx.lifecycle.LiveData
import com.ivzb.arch.data.db.LinkFtsEntity
import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

/**
 * Loads all link items into a list.
 */
open class LoadLinksUseCase @Inject constructor(
    private val repository: LinksRepository
) : UseCase<Unit, List<Link>>() {

    override fun execute(parameters: Unit): List<Link> {
        return repository.getAll()
    }
}
