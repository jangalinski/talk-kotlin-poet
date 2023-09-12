package io.github.jangalinski.talks

import io.github.jangalinski.CodeTalksTalk

object CodeTalksDB {

  private val talks = listOf(
    CodeTalksTalk("Introducing Event Sourcing into the Monolith: A Travelogue", "Our 13-year-old Ruby on Rails app is well-maintained and stable boring. Dealing with many unstable vendors, networks, and co-workers, we’ve struggled to keep track of our processes within the application and maintain an audit trail. We’ve been applying patches to symptoms for too long. When we finally decided to pivot in 2020 and switched a core part of the application to Event Sourcing, we immediately felt the relief of a reliable, traceable process. We also learned our lessons in introducing a pattern like this to an existing app. And now, it’s time to share…"),
    CodeTalksTalk("Code to Cloud with the Azure Developer CLI", "In this talk, Liam will demonstrate how you can take a napkin idea to a production ready system in just a few minutes using the Azure Developer CLI, Infrastructure as Code, GitHub Codespaces and GitHub Copilot. He will show how you can actively reduce the steps you need to take as a developer by using best practices when working on your project and as such speeding up your time to market, improving your DevOps lifecycle and reducing errors along the way. This is also the perfect opportunity to see one of the latest developer friendly CLI tools in action and how you can get hands on yourself.")
  )

  fun findByKeyword(keyword: String) = talks.filter {
    it.name.contains(keyword) || it.description.contains(keyword)
  }
}
