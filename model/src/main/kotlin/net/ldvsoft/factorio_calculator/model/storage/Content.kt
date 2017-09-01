package net.ldvsoft.factorio_calculator.model.storage

import net.ldvsoft.factorio_calculator.model.base.Item
import net.ldvsoft.factorio_calculator.model.base.Machine
import net.ldvsoft.factorio_calculator.model.base.MachineType
import net.ldvsoft.factorio_calculator.model.base.Recipe

interface Content {
    val items: IdMap<Item>
    val machineTypes: IdMap<MachineType>
    val machines: IdMap<Machine>
    val recipes: IdMap<Recipe>
}

interface MutableContent : Content {
    override val items: MutableIdMap<Item>
    override val machineTypes: MutableIdMap<MachineType>
    override val machines: MutableIdMap<Machine>
    override val recipes: MutableIdMap<Recipe>
}

