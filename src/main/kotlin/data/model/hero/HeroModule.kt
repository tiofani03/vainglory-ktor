package com.tiooooo.data.model.hero

import RoleModel
import com.tiooooo.data.model.hero.attack_type.AttackTypeModel
import com.tiooooo.data.model.hero.position.PositionModel
import com.tiooooo.data.model.hero.power_status.PowerStatusModel
import com.tiooooo.data.model.hero.skill_status.SkillStatusModel
import com.tiooooo.data.model.hero.skin_status.SkinStatusModel
import com.tiooooo.data.model.hero.state_color.StateColorModel
import io.ktor.server.application.Application
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

val heroModule = module {
    single { RoleModel(get()) }
    single { AttackTypeModel(get()) }
    single { PositionModel(get()) }
    single { StateColorModel(get()) }
    single { HeroModel(get()) }
    single { PowerStatusModel(get()) }
    single { SkillStatusModel(get()) }
    single { SkinStatusModel(get()) }
}

fun Application.initializeDatabaseModels() {
    val koin = getKoin()
    listOf(
        koin.get<RoleModel>(),
        koin.get<AttackTypeModel>(),
        koin.get<PositionModel>(),
        koin.get<HeroModel>(),
        koin.get<StateColorModel>(),
        koin.get<PowerStatusModel>(),
        koin.get<SkillStatusModel>(),
        koin.get<SkinStatusModel>(),
    ).forEach { it.initialize() }
}




