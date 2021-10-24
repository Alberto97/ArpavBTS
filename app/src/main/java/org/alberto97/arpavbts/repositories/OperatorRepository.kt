package org.alberto97.arpavbts.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.alberto97.arpavbts.db.OperatorDao
import org.alberto97.arpavbts.db.OperatorView
import org.alberto97.arpavbts.models.OperatorModel
import org.alberto97.arpavbts.tools.IOperatorConfig
import javax.inject.Inject
import javax.inject.Singleton

interface IOperatorRepository {
    fun getOperators(value: String?): Flow<List<OperatorModel>>
}

@Singleton
class OperatorRepository @Inject constructor(
    private val config: IOperatorConfig,
    private val operatorDao: OperatorDao
) : IOperatorRepository {

    override fun getOperators(value: String?): Flow<List<OperatorModel>> {
        val flow = if (value.isNullOrEmpty())
            operatorDao.getAll()
        else
            operatorDao.get(value)

        return flow.map { gestori -> mapOperators(gestori) }
    }

    private fun mapOperators(operators: List<OperatorView>): List<OperatorModel> {
        return operators.map { operator ->
            val name = config.getName(operator.name)
            val color = config.getColor(operator.name)
            OperatorModel(name, color, operator.towers, operator.name)
        }
    }
}
