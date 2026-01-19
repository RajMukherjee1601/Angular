export type BookingStatus = 'BOOKED' | 'PAID' | 'PICKED_UP' | 'IN_TRANSIT' | 'DELIVERED' | 'CANCELLED';

export interface Booking {
  bookingId: string;
  customerEmail: string;
  createdByOfficer: boolean;
  senderName: string;
  receiverName: string;
  parcelType: string;
  weightKg: number;
  distanceKm: number;
  baseCost: string;
  adminFee: string;
  totalCost: string;
  status: BookingStatus;
  pickupTime?: string | null;
  dropTime?: string | null;
  createdAt: string;
  updatedAt: string;
}
