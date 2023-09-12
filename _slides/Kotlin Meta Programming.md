---
title: "Kotlin Meta Programming - Code Generation with Kotlin Poet"
center: true
theme: night
transition: slide
---
<!-- slide bg="[[code-talks-start.png]]" -->

Jan Galinski
<br/>
## Code Generation with Kotlin Poet

Backend Applikationen & Services

note: 
* Thanks for being here after the great conference
* endspurt

---
<!-- .slide: data-auto-animate -->

<i class="fas fa-book-open fa-4x"></i>

# Let's start with a story


--
<!-- .slide: data-auto-animate -->
### Girls & Boys Day 2023

![[dinner-process.png]]

ChatGPT powered _what to cook for dinner?_ process

note: girls and boys day 
- fuck
- camunda process & chat gpt
-  if kids can do this --- managers can two as well
-  but: does it work? is the receipe correct? are all items on the grocery shopping list?

--
<!-- .slide: data-auto-animate -->
### Girls & Boys Day 2023

![[dinner-process.png]]

https://github.com/holunda-io/camunda-8-connector-gpt#camunda-8-gpt-ai-connectors-

--

<!-- .slide: data-auto-animate -->
<i class="fas fa-question-circle fa-4x"></i>

## How can we ...

--

<i class="fas fa-question-circle fa-4x"></i>

## How can we ...

- ... reduce manual coding?
+ ... ensure high quality?

--
<!-- .slide: data-auto-animate -->

### Abstract

<small>
AI code generation is a challenge for our daily work. But do we want to rely on some chat comments for professional software development? 
There must be a middle way between manual coding and c&p from the internet. 

Luckily there is: meta-programming. In short: write software that writes software. 
We will have a look at practical implementations using the Kotlin Poet language model lib and maven  plugin infrastructure.
</small>

--

# Let there be code.

---

<!-- slide bg="[[kotlin-bg-black.svg]]" -->

<grid drag="70 100" drop="right">

<i class="fas fa-question-circle fa-4x"></i>

## About me

</grid>

---
<!-- slide bg="[[c64-simon_ist_doof.png]]" style="color:#6C5FB5"-->

+ First program in 1986 - BASIC/C64
+ ... before I even owned the machine

---
<!-- slide bg="[[c64-empty.svg]]" -->

<grid drop="topleft"  style="color:#6C5FB5">

+ Senior Consultant @HolisticonAG
+ 20+ years of industry experience
+ Kotlin - Fan
+ ... Backend/spring boot - no Android
+ ... prefers maven over gradle
+ ... uses light mode

</grid>

<grid  drop="right">
![[jg-comic.png]]
[about.me/jangalinski](https://about.me/jangalinski)
</grid>

---

<!-- slide bg="[[kotlin-bg-black.svg]]" -->

<grid drag="70 100" drop="right">

### Agenda

* What is Meta Programming?
* Why (do I think) is it important?  
* What options do we have?
* How does it work?

</grid>

---


<!-- slide bg="[[images/meta-taal-lake.jpg]]" -->

# What is Meta  Programming?

<grid drop="bottom"  drag="100 6"  >
  <small>image: https://www.filipinotravel.com.ph/lake-taal/</small>
</grid>

---

<grid drop="top" drag="100 40" >
## Examples of Code Generators
</grid>

<grid drop="bottomleft"  >
* WSDL & SOAP
* OpenAPI/swagger
* Avro
* JSON Schema

</grid>

<grid drop="bottomright"  >
* Mapstruct Mappers
* Lombok (Java)
* Jackson Afterburner
</grid>

---
## Types of Code Generation

* Annotation Processing
* Command Line / Console
* Build Plugins (maven/gradle)

* Source Code Generation
  * sun.codemodel
  * roaster
  * java poet
  * spoon
* Byte Code Generation (ASM, ByteBuddy)

---

## I don't like templates

```mustache
{{#useBeanValidation}}
	{{>beanValidation}}
	{{>beanValidationModel}}
{{/useBeanValidation}}
{{#swagger2AnnotationLibrary}}
  @Schema({{#example}}example = "{{#lambdaRemoveLineBreak}}
  {{#lambdaEscapeDoubleQuote}}{{{.}}}{{/lambdaEscapeDoubleQuote}}{{/lambdaRemoveLineBreak}}", 
  {{/example}}required = true, 
  {{#isReadOnly}}readOnly = {{{isReadOnly}}},{{/isReadOnly}}description = "{{{description}}}"){{/swagger2AnnotationLibrary}}
{{#swagger1AnnotationLibrary}}
  @ApiModelProperty({{#example}}example = "{{#lambdaRemoveLineBreak}}{{#lambdaEscapeDoubleQuote}}{{{.}}}{{/lambdaEscapeDoubleQuote}}{{/lambdaRemoveLineBreak}}", {{/example}}required = true, {{#isReadOnly}}readOnly = {{{isReadOnly}}}, {{/isReadOnly}}value = "{{{description}}}"){{/swagger1AnnotationLibrary}}
  
@get:JsonProperty("{{{baseName}}}", required = true){{#isInherited}} override{{/isInherited}} {{>modelMutable}} {{{name}}}: {{#isEnum}}{{#isArray}}{{baseType}}<{{/isArray}}{{#isInherited}}{{parent}}.{{/isInherited}}{{^isInherited}}{{classname}}.{{/isInherited}}{{{nameInCamelCase}}}{{#isArray}}>{{/isArray}}{{/isEnum}}{{^isEnum}}{{{dataType}}}{{/isEnum}}{{#isNullable}}?{{/isNullable}}{{#defaultValue}} = {{{.}}}{{/defaultValue}}

```

---

Kotlin Poet

Non Linear generation

---
# Links

* [kotlin poet](https://square.github.io/kotlinpoet/) - KotlinPoet is a Kotlin and Java API for generating .kt source files. Source file generation can be useful when doing things such as annotation processing or interacting with metadata files (e.g., database schemas, protocol formats). By generating code, you eliminate the need to write boilerplate while also keeping a single source of truth for the metadata.
* [c64 emulator](https://c64emulator.111mb.de/index.php?site=home&group=c64)
* [obsidian - advanced slides](https://mszturc.github.io/obsidian-advanced-slides/)

---

<!-- slide bg="[[no understanding.jpg]]" -->

QUESTIONS?

---

# Unstructured

  

Introduction

  

- basic 10 print Simon ist doof 
- Still uses light mode
- Don’t uses android

  

What is meta programming

Thoughts on Code generation vs ai 

  

Templating vs language model 

  

Kotlin poet basics 

  

Hello world example

  

Reflection. Metadata. Annotation processing

  

Testing

  

Maven/grade integration
