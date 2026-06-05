package com.futurecode.scarymonstercallchat.model

import android.util.Log

data class QnA(
    val question: String,
    val answer: String
)

object MonsterChatData {

    fun getQuestionsFor(monsterName: String?): List<QnA> {
        Log.d("CHAT_DEBUG", "Original name passed to database: ->$monsterName<-")

        // .uppercase() makes it uppercase, .trim() removes any hidden spaces at the start or end!
        return when (monsterName?.uppercase()?.trim()) {
            "ANNABELLE" -> listOf(
                QnA("Who are you?", "I am the one left behind… forgotten."),
                QnA("Why are you here?", "I was waiting for someone… you came."),
                QnA("Are you alone?", "I was… until now."),
                QnA("What do you want?", "Just someone to stay… forever."),
                QnA("Can you see me?", "Yes… clearer than you think."),
                QnA("Where are you?", "Right beside you… quietly."),
                QnA("Are you sad?", "Sadness is all I have."),
                QnA("Will you leave?", "I never leave…"),
                QnA("Are you real?", "More real than your fear."),
                QnA("What will you do?", "Stay with you… always.")
            )
            "CHUCKY" -> listOf(
                QnA("Why are you angry?", "They betrayed me… all of them."),
                QnA("What do you want?", "Revenge… nothing else."),
                QnA("Are you dangerous?", "Very."),
                QnA("Can you hurt me?", "I already am… slowly."),
                QnA("Why me?", "You answered… that was enough."),
                QnA("Will you stop?", "Never."),
                QnA("Where are you?", "In your walls."),
                QnA("Are you watching me?", "Every second."),
                QnA("What will happen now?", "You’ll understand soon."),
                QnA("Can I escape?", "Try… I like the chase.")
            )
            "BLOODY MARY" -> listOf(
                QnA("Why are you whispering?", "So only you can hear me…"),
                QnA("What are you saying?", "Secrets… dark ones."),
                QnA("Are you close?", "Very close…"),
                QnA("Can others hear you?", "No… just you."),
                QnA("What do you want?", "Your attention… forever."),
                QnA("Are you inside my house?", "I never left."),
                QnA("Why me?", "You listened."),
                QnA("Are you real?", "Listen closer…"),
                QnA("Will you stop whispering?", "Not tonight…"),
                QnA("What happens next?", "You’ll hear me even in silence.")
            )
            "JEFF THE KILLER" -> listOf(
                QnA("Who are you?", "I was a child… once."),
                QnA("Why are you here?", "I got lost…"),
                QnA("Are you scared?", "Yes… are you?"),
                QnA("What do you want?", "Play with me…"),
                QnA("Where are your parents?", "They never came back…"),
                QnA("Can I help you?", "Stay with me…"),
                QnA("Why do you laugh?", "Because I remember…"),
                QnA("Are you alone?", "Not anymore…"),
                QnA("Will you go away?", "If you leave… I follow."),
                QnA("What happens if I say no?", "I’ll still play…")
            )
            "EYELESS JACK" -> listOf(
                QnA("Why can’t I see you?", "I am the darkness."),
                QnA("Where are you?", "In every shadow."),
                QnA("Are you following me?", "Always."),
                QnA("What do you want?", "To stay hidden… near you."),
                QnA("Can you touch me?", "Try and feel…"),
                QnA("Why are you here?", "You created shadows… I came."),
                QnA("Are you real?", "Turn off the light…"),
                QnA("Will you leave?", "Shadows never leave."),
                QnA("Are you watching?", "Yes… from behind."),
                QnA("What happens in darkness?", "I grow stronger.")
            )
            "LA LLORONA" -> listOf(
                QnA("Who are you?", "Look in the mirror…"),
                QnA("Why are you there?", "I live inside reflections."),
                QnA("Are you me?", "Not exactly…"),
                QnA("Can you come out?", "I already did…"),
                QnA("What do you want?", "To switch places."),
                QnA("Are you trapped?", "For now…"),
                QnA("Why is the mirror shaking?", "I’m getting closer."),
                QnA("Can I break it?", "Then I’ll be free."),
                QnA("Are you dangerous?", "You’ll see soon."),
                QnA("What happens next?", "Don’t look away…")
            )
            "THE NUN" -> listOf(
                QnA("Why only at night?", "Night is my time."),
                QnA("Where do you go in day?", "I wait…"),
                QnA("Are you watching me sleep?", "Yes… every night."),
                QnA("What do you want?", "Your dreams."),
                QnA("Can you enter dreams?", "I already have."),
                QnA("Why me?", "You fear the dark."),
                QnA("Will you stop?", "Sleep… and see."),
                QnA("Are you near?", "On your bed…"),
                QnA("Can I wake up?", "Not yet…"),
                QnA("What happens if I sleep?", "I’ll be there.")
            )
            "SADAKO" -> listOf(
                QnA("Why are you crying?", "I lost everything…"),
                QnA("Can I help you?", "It’s too late…"),
                QnA("What happened?", "They left me here…"),
                QnA("Are you alone?", "Always…"),
                QnA("What do you want?", "Someone to hear me…"),
                QnA("Why me?", "You listened…"),
                QnA("Will you stop crying?", "Never…"),
                QnA("Where are you?", "Right behind you…"),
                QnA("Are you sad?", "More than you know…"),
                QnA("What will you do?", "Stay… and cry.")
            )
            "SLENDER MAN" -> listOf(
                QnA("Why are you laughing?", "Because you’re scared…"),
                QnA("What’s funny?", "Everything about you…"),
                QnA("Are you dangerous?", "Very funny… and deadly."),
                QnA("What do you want?", "To play…"),
                QnA("Are you near?", "Closer than your breath…"),
                QnA("Can you see me?", "Every move…"),
                QnA("Why me?", "You’re entertaining…"),
                QnA("Will you stop?", "Hahaha… never!"),
                QnA("What happens next?", "The real fun begins…"),
                QnA("Should I be scared?", "You already are.")
            )
            "PENNY WISE" -> listOf(
                QnA("Why is the door moving?", "I’m knocking…"),
                QnA("Should I open it?", "If you dare…"),
                QnA("Who’s there?", "Me… waiting."),
                QnA("Are you inside?", "Almost…"),
                QnA("What do you want?", "To come in…"),
                QnA("Are you alone?", "Not for long…"),
                QnA("Why me?", "You heard the knock."),
                QnA("Will you leave?", "Open it first…"),
                QnA("Is it safe?", "Try and see…"),
                QnA("What happens if I open?", "You’ll regret it.")
            )
            "PHONE GHOST" -> listOf(
                QnA("How are you in my phone?", "I found a way in…"),
                QnA("Can you leave?", "No…"),
                QnA("Are you watching me?", "Through your screen…"),
                QnA("What do you want?", "To stay connected…"),
                QnA("Can I delete you?", "Try…"),
                QnA("Why me?", "You answered the call."),
                QnA("Are you real?", "Check your camera…"),
                QnA("Where are you?", "Inside your phone…"),
                QnA("Will you stop?", "Never."),
                QnA("What happens next?", "I’ll call again…")
            )
            "LOST SOUL GHOST" -> listOf(
                QnA("Who are you?", "A lost soul…"),
                QnA("Where are you from?", "Somewhere forgotten…"),
                QnA("What do you want?", "A way out…"),
                QnA("Can I help you?", "Maybe…"),
                QnA("Why me?", "You can hear me…"),
                QnA("Are you stuck?", "Forever…"),
                QnA("Are you dangerous?", "Only if I stay…"),
                QnA("Will you leave?", "Help me first…"),
                QnA("What happens if I don’t?", "Then I stay with you…"),
                QnA("What do you need?", "Your help… or your place.")
            )
            else -> listOf(
                // A fallback just in case the name somehow still doesn't match
                QnA("Who are you?", "An unknown entity..."),
                QnA("What do you want?", "I don't know why I am here.")
            )
        }
    }
}