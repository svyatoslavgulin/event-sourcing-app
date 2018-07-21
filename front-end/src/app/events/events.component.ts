import {Component, OnInit} from '@angular/core';
import {Event} from "../event";
import {HeroService} from "../hero.service";

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[];

  constructor(private heroService: HeroService) {
  }

  ngOnInit() {
    this.getAllEvents();
  }

  getAllEvents(): void {
    this.heroService.getAllEvents().subscribe(events => this.events = events);
  }

}
