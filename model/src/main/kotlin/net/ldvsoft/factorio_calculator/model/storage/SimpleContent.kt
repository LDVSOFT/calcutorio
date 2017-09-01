package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.Item
import net.ldvsoft.factorio_calculator.model.base.Machine
import net.ldvsoft.factorio_calculator.model.base.MachineType
import net.ldvsoft.factorio_calculator.model.base.Recipe

class SimpleContent: MutableContent {
    override val items = SimpleIdMap(listener = object : SimpleIdMap.Listener<Item>() {
        override fun onRemove(element: Item) {
            if (recipes.any { element in it.ingredients || element in it.products })
                throw IllegalStateException("There are recipes with that item ($element)")
        }
    })

    override val machineTypes = SimpleIdMap(listener = object : SimpleIdMap.Listener<MachineType>() {
        override fun onRemove(element: MachineType) {
            if (machines.any { it.type == element })
                throw IllegalStateException("There are machines with that type ($element)")
            if (recipes.any { it.requiredMachineLevel.containsKey(element) })
                throw IllegalStateException("There are recipes dependant on that type ($element)")
        }
    })

    //FIXME Explicit type only because Kotlin is not powerful enough
    override val machines: SimpleIdMap<Machine> = SimpleIdMap(listener = object : SimpleIdMap.Listener<Machine>() {
        override fun onAdd(element: Machine) {
            machineTypes.add(element.type)
        }
    })

    override val recipes: SimpleIdMap<Recipe> = SimpleIdMap(listener = object : SimpleIdMap.Listener<Recipe>() {
        override fun onAdd(element: Recipe) {
            machineTypes.addAll(element.requiredMachineLevel.keys)
            items.addAll(element.ingredients.keys)
            items.addAll(element.products.keys)
        }
    })
}