package net.ldvsoft.calcutorio.model.storage

import com.fasterxml.jackson.annotation.JsonCreator
import net.ldvsoft.calcutorio.model.base.Item
import net.ldvsoft.calcutorio.model.base.Machine
import net.ldvsoft.calcutorio.model.base.MachineType
import net.ldvsoft.calcutorio.model.base.Recipe

class SimpleContent() : MutableContent {
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
            if (recipes.any { element in it.requiredMachineLevel })
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

    @JsonCreator
    constructor(
            items: Collection<Item>, machineTypes: Collection<MachineType>,
            machines: Collection<Machine>, recipes: Collection<Recipe>
    ): this() {
        this.items.addAll(items)
        this.machineTypes.addAll(machineTypes)
        this.machines.addAll(machines)
        this.recipes.addAll(recipes)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleContent

        if (items != other.items) return false
        if (machineTypes != other.machineTypes) return false
        if (machines != other.machines) return false
        if (recipes != other.recipes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = items.hashCode()
        result = 31 * result + machineTypes.hashCode()
        result = 31 * result + machines.hashCode()
        result = 31 * result + recipes.hashCode()
        return result
    }

    override fun toString(): String {
        return "SimpleContent(items=$items, machineTypes=$machineTypes, machines=$machines, recipes=$recipes)"
    }
}