import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SpoolListComponent } from './spool-list'; // <-- Updated import

describe('SpoolListComponent', () => {
  let component: SpoolListComponent; // <-- Updated type
  let fixture: ComponentFixture<SpoolListComponent>; // <-- Updated type

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpoolListComponent] // <-- Updated import
    })
    .compileComponents();

    fixture = TestBed.createComponent(SpoolListComponent); // <-- Updated component
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});