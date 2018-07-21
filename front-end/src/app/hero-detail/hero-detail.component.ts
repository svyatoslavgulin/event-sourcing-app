import {Component, OnInit, Input} from '@angular/core';
import {Hero} from '../hero';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';

import {HeroService} from '../hero.service';
import {Event} from "../event";

@Component({
  selector: 'app-hero-detail',
  templateUrl: './hero-detail.component.html',
  styleUrls: ['./hero-detail.component.css']
})

export class HeroDetailComponent implements OnInit {
  @Input() hero: Hero;
  events: Event[];

  constructor(
    private route: ActivatedRoute,
    private heroService: HeroService,
    private location: Location
  ) {
  }

  ngOnInit(): void {
    this.getHero();
    this.getEvents();
  }

  save(): void {
    this.heroService.updateHero(this.hero)
      .subscribe(() => this.goBack());
  }

  getHero(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.heroService.getHero(id)
      .subscribe(hero => this.hero = hero);
  }

  getEvents(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.heroService.getEventsForHero(id)
      .subscribe(events => this.events = events);
  }

  goBack(): void {
    this.location.back();
  }
}
